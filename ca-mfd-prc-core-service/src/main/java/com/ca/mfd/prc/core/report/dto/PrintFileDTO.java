package com.ca.mfd.prc.core.report.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 打印文件dto
 *
 * @author jay.he
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@Schema(description = "PrintFileListDTO")
public class PrintFileDTO implements Serializable {

    private String prefix;

    private String name;

    private List<ReportFiles> reportFiles;

    @Data
    public static class ReportFiles {
        private String name;

        private String updateDate;
    }
}
