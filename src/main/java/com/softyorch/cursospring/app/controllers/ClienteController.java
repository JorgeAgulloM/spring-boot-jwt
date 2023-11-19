package com.softyorch.cursospring.app.controllers;

import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.service.IClienteService;
import com.softyorch.cursospring.app.service.IUploadFileService;
import com.softyorch.cursospring.app.util.paginator.PageRender;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import static com.softyorch.cursospring.app.util.Constants.UPLOADS_FOLDER;

@Controller
@SessionAttributes("cliente")
//para poder obtener guardar los datos de sesión del cliente y obtener el id para la edición.
public class ClienteController {

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IUploadFileService uploadFileService;

    @GetMapping(value = "/" + UPLOADS_FOLDER + "/{filename:.+}") // con la expresión regular :.+ evitamos que spriong trunque la extensión de la imagen.
    public ResponseEntity<Resource> showPhoto(@PathVariable String filename) {

        Resource resource;

        try {
            resource = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }


    @GetMapping(value = "/detail/{id}")
    public String detail(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Cliente cliente = clienteService.fetchClienteByIdWithFacturas(id); //findOne(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("title", "Detalle cliente: " + cliente.getNombre());

        return "detail";
    }

    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
        
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {

        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de cliente");
        return "form";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null){
                flash.addFlashAttribute("error", "El ID del cliente no existe en la base de datos");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del cliente no puede ser 0");
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Editar cliente");
        return "form";
    }

    /* en caso de que se llame la método con errores del formulario, este volverá a lanzar el formulario con los valores
    // que tenía insertados por el usuario, siempre y cuando la clase de la Entity entity se llame igual que el nombre
    // que estamos pasando al formulario, en caso de que no sea así, se puede validar con @ModelAttribute("cliente")
    */
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(
            @Valid Cliente cliente,
            BindingResult result,
            Model model,
            @RequestParam("file") MultipartFile photo,
            RedirectAttributes flash,
            SessionStatus status
    ) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        if (!photo.isEmpty()) {

            if (cliente.getId() != null &&
                    cliente.getId() > 0 &&
                    cliente.getPhoto() != null &&
                    !cliente.getPhoto().isEmpty()
            ) {
                uploadFileService.delete(cliente.getPhoto());
            }

            String uniqueFilename;
            try {
                uniqueFilename = uploadFileService.copy(photo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            flash.addFlashAttribute(
                    "info",
                    "Foto " + uniqueFilename + " subida correctamente."
            );
            cliente.setPhoto(uniqueFilename);
        }

        String msgFlash = (cliente.getId() != null) ? "editado" : "creado";

        clienteService.save(cliente);
        status.setComplete(); //Con esto reseteamos los datos de sesión
        flash.addFlashAttribute("success", "Cliente " + msgFlash + " con exito!!");
        return "redirect:listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado!!");

            if (uploadFileService.delete(cliente.getPhoto())) {
                flash.addFlashAttribute(
                        "info",
                        "Foto " + cliente.getPhoto() + " eliminada con éxisto!"
                );
            }

        }

        return "redirect:/listar";
    }

}
