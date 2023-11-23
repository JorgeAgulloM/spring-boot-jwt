package com.softyorch.cursospring.app.view.json;

import com.softyorch.cursospring.app.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

@SuppressWarnings("unchecked")
@Component(value = "listar")
public class ClienteListJsonView extends MappingJackson2JsonView {
    @Override
    protected Object filterModel(Map<String, Object> model) {
        model.remove("title");
        model.remove("page");

        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
        model.remove("clientes");

        model.put("clientes", clientes.getContent());

        return super.filterModel(model);
    }
}
