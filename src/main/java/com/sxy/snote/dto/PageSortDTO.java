package com.sxy.snote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageSortDTO {
    private int pageNo;
    private int pageSize;
    private boolean asc;
    private String arg;
}
