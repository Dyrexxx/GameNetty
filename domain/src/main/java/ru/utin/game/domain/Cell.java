package ru.utin.game.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cell {
    private Integer size;
    private Integer x;
    private Integer y;
    private TypeCell typeCell;

    public Cell(int x, int y, int size) {
        this.size = size;
        this.x = x;
        this.y = y;
    }


}
