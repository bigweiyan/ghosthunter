package com.wxyz.glbasics.gamedev2d;

public class GridHashGrid {
	int cellsPerRow;
	int cellsPerCol;
	int cellSize;
	int[] cellIds = new int[4];

	@SuppressWarnings("unchecked")
	public GridHashGrid(int worldWidth,int worldHeight,int cellSize,int capacity){
		this.cellSize = cellSize;
		this.cellsPerRow = worldWidth / cellSize +1;
		this.cellsPerCol = worldHeight / cellSize +1;
		int numCells = cellsPerRow * cellsPerCol;
	}
	

	private int[] getCellIds(StaticGridObject obj) {
		int x = obj.x / cellSize;
		int y = obj.y / cellSize;
		int i = 0;
		cellIds[i++] = x + y * cellsPerRow;
		if(obj.x % cellSize == 0 && x > 0){
			cellIds[i++] = x + y * cellsPerRow - 1;
			if(obj.y % cellSize == 0 && y > 0){
				cellIds[i++] = x + (y - 1) * cellsPerRow;
				cellIds[i++] = x - 1 + (y - 1) * cellsPerRow;
			}else if((obj.y+1) % cellSize == 0 && y < cellsPerCol - 1){
				cellIds[i++] = x + (y + 1) * cellsPerRow;
				cellIds[i++] = x - 1 + (y + 1) * cellsPerRow;
			}
		}else if((obj.x+1) % cellSize == 0 && x < cellsPerRow - 1){
			cellIds[i++] = x + y * cellsPerRow + 1;
			if(obj.y % cellSize == 0 && y > 0){
				cellIds[i++] = x + (y - 1) * cellsPerRow;
				cellIds[i++] = x + 1 + (y - 1) * cellsPerRow;
			}else if((obj.y+1) % cellSize == 0 && y < cellsPerCol - 1){
				cellIds[i++] = x + (y + 1) * cellsPerRow;
				cellIds[i++] = x + 1 + (y + 1) * cellsPerRow;
			}
		}else{
			if(obj.y % cellSize == 0 && y > 0){
				cellIds[i++] = x + (y - 1) * cellsPerRow;
			}else if((obj.y+1) % cellSize == 0 && y < cellsPerCol - 1){
				cellIds[i++] = x + (y + 1) * cellsPerRow;
			}
		}
		while(i<=3) cellIds[i++] = -1;
		return cellIds;
	}

	private int[] getCellIds(GameObject obj) {
		int x1 = (int)Math.floor(obj.bounds.lowerLeft.x / cellSize);
		int y1 = (int)Math.floor(obj.bounds.lowerLeft.y / cellSize);
		int x2 = (int)Math.floor((obj.bounds.lowerLeft.x + obj.bounds.width)/cellSize);
		int y2 = (int)Math.floor((obj.bounds.lowerLeft.y + obj.bounds.height)/cellSize);
		if(x1==x2 && y1==y2){
			if(x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y2 < cellsPerCol)
				cellIds[0] = x1 + y1 * cellsPerRow;
			else
				cellIds[0] = -1;
			cellIds[1] = -1;
			cellIds[2] = -1;
			cellIds[3] = -1;
		}else if(x1 == x2){
			int i=0;
			if(x1 >= 0 && x1 < cellsPerRow){
				if(y1 >= 0 && y1 < cellsPerCol)
					cellIds[i++] = x1 + y1 * cellsPerRow;
				if(y2 >=0 && y2 < cellsPerCol)
					cellIds[i++] = x1 + y2 * cellsPerRow; 
			}
			while(i<=3) cellIds[i++] = -1;
		}else if(y1 == y2){
			int i=0;
			if(y1 >= 0 && y1 < cellsPerCol){
				if(x1 >= 0 && x1 < cellsPerRow)
					cellIds[i++] = x1 + y1 * cellsPerRow;
				if(x2 >= 0 && x2 < cellsPerRow)
					cellIds[i++] = x2 + y1 * cellsPerRow;
			}
			while(i<=3) cellIds[i++] = -1;
		}else{
			int i = 0;
			int y1CellsPerRow = y1 * cellsPerRow;
			int y2CellsPerRow = y2 * cellsPerRow;
			if(x1 >= 0 && x1 < cellsPerRow && y1>=0 && y1<cellsPerCol)
				cellIds[i++] = x1 + y1CellsPerRow;
			if(x2 >= 0 && x2 < cellsPerRow && y1>=0 && y1<cellsPerCol)
				cellIds[i++] = x2 + y1CellsPerRow;
			if(x1 >= 0 && x1 < cellsPerRow && y2>=0 && y2<cellsPerCol)
				cellIds[i++] = x1 + y2CellsPerRow;
			if(x2 >= 0 && x2 < cellsPerRow && y2>=0 && y2<cellsPerCol)
				cellIds[i++] = x2 + y2CellsPerRow;
			while(i<=3) cellIds[i++] = -1;
		}
		return cellIds;
	}
}
