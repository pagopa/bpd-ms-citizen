package eu.sia.bpd.demo.web.controller.model.dto;

import java.io.Serializable;

public interface DTO<T extends Serializable> {

    T toEntity();

}
