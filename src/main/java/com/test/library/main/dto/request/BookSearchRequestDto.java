package com.test.library.main.dto.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BookSearchRequestDto implements BasePagingRequestDto {
    private Integer page;
    private Integer pagesize;
    private String title;
    private String author;
    private Boolean isUnreturned;
    private LocalDate checkOutStart;
    private LocalDate checkOutEnd;
    private LocalDate expectedReturnStart;
    private LocalDate expectedReturnEnd;
    private LocalDate returnStart;
    private LocalDate returnEnd;
}
