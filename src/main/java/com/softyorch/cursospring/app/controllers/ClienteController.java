package com.softyorch.cursospring.app.controllers;

import com.softyorch.cursospring.app.models.entity.Cliente;
import com.softyorch.cursospring.app.service.IClienteService;
import com.softyorch.cursospring.app.service.IUploadFileService;
import com.softyorch.cursospring.app.util.paginator.PageRender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import static com.softyorch.cursospring.app.util.Constants.UPLOADS_FOLDER;

@Controller
@SessionAttributes("cliente")
//para poder obtener guardar los datos de sesión del cliente y obtener el id para la edición.
public class ClienteController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private MessageSource messages;

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/" + UPLOADS_FOLDER + "/{filename:.+}")
    // con la expresión regular :.+ evitamos que spriong trunque la extensión de la imagen.
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

    @PreAuthorize("hasRole('ROLE_USER')") //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/detail/{id}")
    public String detail(@PathVariable(value = "id") Long id,
                         Map<String, Object> model,
                         RedirectAttributes flash,
                         Locale locale
    ) {
        Cliente cliente = clienteService.fetchClienteByIdWithFacturas(id); //findOne(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", messages.getMessage("text.cliente.flash.db.error", null, locale));
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("title", messages.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ").concat(cliente.getNombre()));

        return "detail";
    }

    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model,
            Authentication authentication,
            HttpServletRequest request,
            Locale locale
    ) {

        if (authentication != null) {
            logger.info("Hola usuario autenticado, tu nombre es: ".concat(authentication.getName()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            logger.info("De forma estática: Hola usuario autenticado, tu nombre es: ".concat(auth.getName()));
            if (hasRole("ROLE_ADMIN")) {
                logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso."));
            } else {
                logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
            }

            SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");

            if (securityContext.isUserInRole("ROLE_ADMIN")) {
                logger.info("SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" tienes acceso."));
            } else {
                logger.info("SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
            }

            if (request.isUserInRole("ROLE_ADMIN")) {
                logger.info("HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso."));
            } else {
                logger.info("HttpServletRequest: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
            }
        }


        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", messages.getMessage("text.cliente.listar.titulo", null, locale));
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {

        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de cliente");
        return "form";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
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
    @Secured("ROLE_ADMIN")
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

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {

        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", messages.getMessage("text.cliente.flash.eliminar.success", null, locale));

            if (uploadFileService.delete(cliente.getPhoto())) {
                String mensajeFotoEliminar = String.format(
                        messages.getMessage("text.cliente.flash.foto.eliminar.success", null, locale),
                        cliente.getPhoto()
                );
                flash.addFlashAttribute("info", mensajeFotoEliminar);

            }

        }

        return "redirect:/listar";
    }

    private boolean hasRole(String role) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return false;

        Authentication auth = context.getAuthentication();
        if (auth == null) return false;

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        return authorities.contains(new SimpleGrantedAuthority(role));

/*        for (GrantedAuthority authority: authorities) {
            logger.info("Hola usuario ".concat(auth.getName()).concat(" tu ROLE es: ").concat(authority.getAuthority()));
            if (role.equals(authority.getAuthority())) return true;
        }

        return false;*/


    }

}
