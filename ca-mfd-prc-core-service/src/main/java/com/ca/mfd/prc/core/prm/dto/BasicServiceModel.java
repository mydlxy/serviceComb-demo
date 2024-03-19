package com.ca.mfd.prc.core.prm.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author inkelink
 */
@Data
public class BasicServiceModel<T> implements Serializable {

    private T Service;
}
