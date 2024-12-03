package ru.utin.game.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CellPosition {
    private int x;
    private int y;
    private TypeCell typeCell;

    public CellPosition(int x, int y, TypeCell typeCell) {
        this.x = x;
        this.y = y;
        this.typeCell = typeCell;
    }
}
