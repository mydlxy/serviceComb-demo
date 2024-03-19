package com.ca.mfd.prc.core.fs.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(title = "文件目录树")
public class DirectoryNode {

    @Schema(title = "目录名称")
    private final String name;

    @Schema(title = "目录路径")
    private final String path;

    @Schema(title = "安全策略")
    private final String strategy;

    @Getter
    @Schema(title = "子目录树")
    private final List<DirectoryNode> children;

    public DirectoryNode(String name, String path, String strategy) {
        this.name = name;
        this.path = path;
        this.strategy = strategy;
        this.children = new ArrayList<>();
    }

    public void addChild(DirectoryNode child) {
        children.add(child);
    }

    public DirectoryNode findChildByName(String name) {
        for (DirectoryNode child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }
}
