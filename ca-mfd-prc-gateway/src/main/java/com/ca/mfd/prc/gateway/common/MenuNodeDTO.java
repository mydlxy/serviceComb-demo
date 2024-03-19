//package com.ca.mfd.prc.common;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * 菜单信息
// *
// * @author inkelink eric.zhou@hg2mes.com
// * @since 1.0.0 2023-08-06
// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//public class MenuNodeDTO implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @JsonProperty("id")
//    public String id;
//
//    @JsonProperty("label")
//    public String label;
//
//    @JsonProperty("icon")
//    public String icon;
//
//    @JsonProperty("isCurrent")
//    public Boolean isCurrent;
//
//    @JsonProperty("routerLink")
//    public String routerLink;
//
//    @JsonProperty("url")
//    public String url;
//
//    @JsonProperty("target")
//    public String target;
//
//    @JsonProperty("isLeaf")
//    public Boolean isLeaf;
//
//    @JsonProperty("items")
//    public List<MenuNodeDTO> items;
//
//}
