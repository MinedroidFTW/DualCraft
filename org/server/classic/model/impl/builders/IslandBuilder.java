package dualcraft.org.server.classic.model.impl.builders;

/*License
====================
Copyright (c) 2010-2012 Daniel Vidmar

We use a modified GNU gpl v 3 license for this.

GNU gpl v 3 is included in License.txt

The modified part of the license is some additions which state the following:

"Redistributions of this project in source or binary must give credit to UnXoft Interactive and DualCraft"
"Redistributions of this project in source or binary must modify at least 300 lines of code in order to release
an initial version. This will require documentation or proof of the 300 modified lines of code."
"Our developers reserve the right to add any additions made to a redistribution of DualCraft into the main
project"
"Our developers reserver the right if they suspect a closed source software using any code from our project
to request to overview the source code of the suspected software. If the owner of the suspected software refuses 
to allow a devloper to overview the code then we shall/are granted the right to persue legal action against
him/her"*/

import java.util.LinkedList;
import java.util.ArrayList;
import java.awt.Point;
import java.lang.Math;

import dualcraft.org.server.classic.model.Builder;
import dualcraft.org.server.classic.model.BlockConstants;
import dualcraft.org.server.classic.model.Position;
import dualcraft.org.server.classic.model.Level;

/**
 * Builds a level.
 * 
 */

public class IslandBuilder extends Builder {

	private int[][] m_contour;

	int m_scale = 1;


	public IslandBuilder(Level level) {
		super(level);
		m_contour = new int[m_width][m_height];
	}

	public void generate() {
		//sculptHills(1000);
		//carveLake(m_width/4, m_height/2);
		//m_antiAlias = true;
		//carveLake(m_width/2, m_height/2);
		//carveCanyon();
		raiseTerrain();
		applyContour();
		generateCaverns(100);
		buildLavaBed(2);
		simulateOceanFlood();
		plantTrees();
	}

	public void plantTrees() {
		ArrayList<Position> treeList = new ArrayList<Position>();
		for(int x = 0;x<m_width;x++) {
			for(int y = 0;y<m_height;y++) {
				boolean tooClose = false;
				for(Position p : treeList) {
					double distance = Math.sqrt(Math.pow(p.getX()-x,2)+Math.pow(p.getY()-y,2));
					if (distance < 30)
						tooClose = true;
				}
				if (!tooClose) {
					if (m_random.nextInt(100) <= 5) {
						for(int z = m_depth-1;z>0;z--) {
							if ((m_blocks[x][y][z] == BlockConstants.DIRT || m_blocks[x][y][z] == BlockConstants.GRASS) && m_blocks[x][y][z+1] == BlockConstants.AIR) {
								plantTree(x, y, z);
								treeList.add(new Position(x, y, z));
								break;
							} else if (z < m_depth-1 && m_blocks[x][y][z+1] != BlockConstants.AIR) {
								break;
							}
						}
					}
				}
			}
		}
	}

	public void plantTree(int rootX, int rootY, int rootZ) {
		for(int z = rootZ;z < rootZ + 5;z++) {
			m_blocks[rootX][rootY][z] = BlockConstants.TREE_TRUNK;
		}
		for(int width = 4;width>0;width--) {
			leafLayer(rootX, rootY, rootZ+5+(4-width), width);
		}
	}

	public void leafLayer(int cx, int cy, int cz, int width) {
		for(int x = Math.max(0, cx-width);x<Math.min(m_width, cx+width);x++) {
			for(int y = Math.max(0, cy-width);y<Math.min(m_height, cy+width);y++) {
				m_blocks[x][y][cz] = BlockConstants.LEAVES;
			}
		}
	}

	public boolean findWater(int x, int y, int distance) {
		return true;
		/*
		if (!(x > 0 && x < m_width && y > 0 && y < m_height))
			return false;
		if (distance == 0)
			return false;
		for (int z = m_depth-1;z>0;z--) {
			if (m_blocks[x][y][z] == BlockConstants.WATER)
				return true;
			if (m_blocks[x][y][z] != BlockConstants.AIR && m_blocks[x][y][z+1] == BlockConstants.AIR)
				return findWater(x+1, y, distance-1) || findWater(x-1, y, distance-1) || findWater(x, y-1, distance-1) || findWater(x, y+1, distance-1);
		}
		return false;
		*/
	}

	public void raiseTerrain() {
		boolean[][] prevContour = new boolean[m_width][m_height];
		boolean[][] curContour = new boolean[m_width][m_height];
		for(int x = 0;x<m_width;x++) {
			for(int y = 0;y<m_height;y++) {
				curContour[x][y] = (m_random.nextInt(100) <= 48);
			}
		}
		for(int count = 0;count<4;count++) {
			System.arraycopy(curContour, 0, prevContour, 0, curContour.length);
			for(int x = 0;x<m_width;x++) {
				for(int y = 0;y<m_width;y++) {
					curContour[x][y] = simulateCell(prevContour, x, y);
				}
			}
		}
		int x = 0;
		int y = 0;
		boolean[][] visited = new boolean[m_width][m_height];
		while(y < m_height) {
			LinkedList<Position> path = new LinkedList<Position>();
			if (curContour[x][y]) {
				Position pos = new Position(x, y, 0);
				path.offer(pos);
			} else {
				m_contour[x][y] = -3;
			}
			int height = m_random.nextInt(6);
			PATH: while (path.size() > 0) {
				Position cur = path.remove();
				if (visited[cur.getX()][cur.getY()])
					continue PATH;
				if (!curContour[cur.getX()][cur.getY()])
					continue PATH;
				visited[cur.getX()][cur.getY()] = true;
				m_contour[cur.getX()][cur.getY()] = height;
				if (cur.getX() > 0 && cur.getX() < m_width-1) {
					path.offer(new Position(cur.getX()-1, cur.getY(), 0));
					path.offer(new Position(cur.getX()+1, cur.getY(), 0));
				}
				if (cur.getY() > 0 && cur.getY() < m_width-1) {
					path.offer(new Position(cur.getX(), cur.getY()-1, 0));
					path.offer(new Position(cur.getX(), cur.getY()+1, 0));
				}
			}
			x++;
			if (x == m_height) {
				x = 0;
				y++;
			}
		}
		for(x =0;x<m_width;x++) {
			for(y = 0;y<m_height;y++) { 
				if (curContour[x][y])
					averageArea(x, y, 2);
			}
		}
	}

	private boolean simulateCell(boolean[][] grid, int x, int y) {
		int count = 0;
		boolean isAlive = grid[x][y];
		if (isAlive)
			count++;
		if (x > 0 && x < m_width-1) {
			if (grid[x-1][y])
				count++;
			if (grid[x+1][y])
				count++;
		}

		if (y > 0 && y < m_height-1) {
			if (grid[x][y+1])
				count++;
			if (grid[x][y-1])
				count++;
		}

		if (y > 0) {
			if (x > 0)
				if (grid[x-1][y-1])
					count++;
			if (x < m_width-1)
				if (grid[x+1][y-1])
					count++;
		}
		if (y < m_height-1) {
			if (x > 0)
				if (grid[x-1][y+1])
					count++;
			if (x < m_width-1)
				if (grid[x+1][y+1])
					count++;
		}

		return count >= 5;
	}

	public void applyContour() {
		int maxHeight = 1;
		for (int x = 0;x<m_width;x++) {
			for (int y = 0;y < m_height;y++) {
				if (m_contour[x][y] > maxHeight)
					maxHeight = m_contour[x][y];
			}
		}
		m_logger.debug("Applying contour");
		for(int x = 0; x < m_width; x++) {
			for(int y = 0; y < m_height; y++) {
				int h = Math.max(0, Math.min(m_depth-1, (m_depth/2) + m_contour[x][y]));
				//int d = m_random.nextInt(8) - 4;
				for(int z = 0; z < m_depth; z++) {
					int type = BlockConstants.AIR;
					if (z >= h && z < m_depth/2-1) {
						type = BlockConstants.WATER;
					} else if (z >= h) {
						type = BlockConstants.AIR;
					} else if(z == (h - 1)) {
						type = BlockConstants.GRASS;
					} else if(z < (h - 1) && z > (h -5 )) {
						type = BlockConstants.DIRT;
					} else if(z <= (h - 5 )) {
						type = BlockConstants.ROCK;
					}
					m_blocks[x][y][z] = (byte) type;
				}
			}
		}
	}

	public void sculptHill(int centerX, int centerY, int height, int radius) {
		sculptHill(centerX, centerY, height, radius, false);
	}

	public void sculptHill(int centerX, int centerY, int height, int radius, boolean additive) {
		//int maxHeight = 1;
		if (additive)
			m_contour[centerX][centerY] += height;
		else
			m_contour[centerX][centerY] = height;

		for(int x = centerX-radius;x < centerX+radius;x++) {
			for(int y = centerY-radius;y < centerY+radius;y++) {
				double distance = Math.sqrt(Math.pow(x-centerX,2)+Math.pow(y-centerY,2));
				if (Math.abs(radius-distance) <= 1)
					interpolateLine(x, y, centerX, centerY);
			}
		}
	}

	private boolean m_antiAlias = false;

	private int ceilInt(double val) {
		return (int)(Math.ceil(Math.abs(val))*((val < 0) ? -1 : 1));
	}

	private void interpolateLine(int startX, int startY, int destX, int destY) {
		while (startX < 0 || startY < 0 || startX > m_width-1 || startY > m_height-1) {
			double direction = Math.atan2(destY-startY, destX-startX);
			startX += ceilInt(Math.cos(direction));
			startY += ceilInt(Math.sin(direction));
		}
		int startHeight = m_contour[startX][startY];
		int endHeight = m_contour[destX][destY];
		if (startHeight == endHeight)
			return;
		double distance = Math.sqrt(Math.pow(startX-destX,2)+Math.pow(startY-destY,2));
		double realX = startX;
		double realY = startY;
		double value = 1;
		while (value > 0) {
			if (Math.abs(destX-realX) < 1.2 && Math.abs(destY-realY) < 1.2)
				break;

			double direction = Math.atan2(destY-realY, destX-realX);
			value = Math.sqrt(Math.pow(realX-destX,2)+Math.pow(realY-destY,2))/distance;
			double height;

			if (value < 0.5)
				height = (startHeight-endHeight)/2*Math.pow(value*2,3) + endHeight;
			else
				height = (startHeight-endHeight)/2*(Math.pow(value*2-2,3) + 2) + endHeight;


			double dx = Math.cos(direction);
			double dy = Math.sin(direction);
			if (dx == -1 || dx == 1)
				dy = 0;
			if (dy == -1 || dy == 1)
				dx = 0;

			realX += ceilInt(dx);
			realY += ceilInt(dy);
			int centerX  = ceilInt(realX);
			int centerY = ceilInt(realY);

			double dTopX = Math.cos(direction+Math.PI/2);
			double dTopY = Math.sin(direction+Math.PI/2);
			int topX = ceilInt(realX+dTopX);
			int topY = ceilInt(realY+dTopY);

			double dBottomX = Math.cos(direction-Math.PI/2);
			double dBottomY = Math.sin(direction-Math.PI/2);
			int bottomX = ceilInt(realX+dBottomX);
			int bottomY = ceilInt(realY+dBottomY);

			if (m_antiAlias) {
				m_contour[centerX][centerY] = (int)(Math.sqrt(Math.pow(centerX-realX,2)+Math.pow(centerY-realY,2))*height);
				m_contour[topX][topY] = (int)(Math.sqrt(Math.pow(topX-realX,2)+Math.pow(topY-realY,2))*height);
				m_contour[bottomX][bottomY] = (int)(Math.sqrt(Math.pow(bottomX-realX,2)+Math.pow(bottomY-realY,2))*height);
			} else {
				m_contour[centerX][centerY] = (int)height;
			}
		}
	}

	private void averageArea(int sx, int sy, int radius) {
		for(int x = Math.max(0, sx - radius);x< Math.min(m_width, sx+radius);x++) {
			for(int y = Math.max(0, sy-radius);y<Math.min(m_height, sy+radius);y++) {
				m_contour[x][y] = (m_contour[sx][sy]+m_contour[x][y])/2;
			}
		}
	}

	public void sculptHills(int iterations) {
		for(int i = 0; i < iterations; i++) {
			if (i % 1000 == 0)
				m_logger.debug("Sculpting hills: "+i+"/"+iterations);
			int x = m_random.nextInt(m_width);
			int y = m_random.nextInt(m_height);
			int height = (m_random.nextInt(10)-5);
			int radius = m_random.nextInt(20) + 15;
			sculptHill(x, y, height, radius);
		}
	}

	public void generateCaverns(int count) {
		for (int i = 0; i < count;i++) {
			m_logger.debug("Generating underground erosion bubbles: "+i+"/"+count);
			int x = m_random.nextInt(m_width);
			int y = m_random.nextInt(m_height);
			int z = m_random.nextInt(m_depth/4);
			int radius = m_random.nextInt(60)+40;
			radius = 6;
			int type = m_random.nextInt(100);
			if (type > 90)
				type = BlockConstants.LAVA;
			else if (type > 45)
				type = BlockConstants.AIR;
			else
				type = BlockConstants.WATER;
			for (int m = 0;m < 2; m++) {
				BUBBLE_GEN: for(int j = x-radius;j<x+radius*2;j++) {
					if (j < 0)
						j = 0;
					if (j >= m_width)
						break BUBBLE_GEN;
					for(int k = y-radius;k<y+radius*2;k++) {
						if (k < 0)
							k = 0;
						if (k >= m_height)
							break BUBBLE_GEN;
						for (int l = z-radius;l<z+radius;l++) {
							if (l < 0)
								l = 0;
							if (l >= m_depth)
								break BUBBLE_GEN;
							double distance = Math.sqrt(Math.pow(j-x, 2)+Math.pow(k-y, 2)+Math.pow(l-z, 2));
							if (Math.abs(distance/radius) <= Math.abs(m_random.nextGaussian())) {
								m_blocks[j][k][l] = (byte) type;
							}
						}
					}
				}
				x++;
			}
		}
	}

	public void buildLavaBed(int depth) {
		m_logger.debug("Building lava bed.");
		for (int z = 0;z < depth; z++) {
			for(int x = 0;x < m_width; x++) {
				for (int y = 0; y < m_height; y++ ) {
					m_blocks[x][y][z] = (byte) BlockConstants.LAVA;
				}
			}
		}
	}

	public void carveLake(int x, int y) {
		int avgDepth = (m_random.nextInt(6)+8);
		int radius = (m_random.nextInt(30)+30);
		carveLake(x, y, new ArrayList<Position>(), radius, avgDepth);
		//int edgeHeight = avgDepth;
	}

	private void carveLake(int x, int y, ArrayList<Position> visited, int distance, int depth) {
		if (distance == 0)
			return;
		if (x < 0 || y < 0 || x >= m_width || y >= m_height)
			return;
		if (depth <= 0)
			return;
		Position cur = new Position(x, y, 0);
		for(Position p : visited)
			if (p.equals(cur))
				return;
		visited.add(cur);

		int delta = m_random.nextInt(depth);

		carveLake(x+1, y, visited, distance-1, delta);
		carveLake(x-1, y, visited, distance-1, delta);
		carveLake(x, y+1, visited, distance-1, delta);
		carveLake(x, y-1, visited, distance-1, delta);
		sculptHill(x, y, -depth, distance);
	}

	public void carveCanyon() {
		int startX = m_random.nextInt(m_width);
		int startY = m_random.nextInt(m_height);
		startX = m_width/2;
		startY = m_height/2;
		double direction = m_random.nextDouble()*Math.PI*2;
		direction = 0;
		int depth = m_random.nextInt(4)+4;
		depth = 5;
		carveCanyon(startX, startY, direction, depth);
		//carveCanyon(startX, startY, m_random.nextDouble()*Math.PI*2, depth);
	}

	private void carveCanyon(int x, int y, double direction, int depth) {
		int nextX = x;
		int nextY = y;
		while (nextX > 0 && nextY > 0 && nextY < m_height-1 && nextX < m_width-1 && depth > 0) {
			sculptHill(nextX, nextY, -depth, 10, true);
			/*for(int i = Math.max(0,nextX-6);i<Math.min(m_width-1, nextX+6);i++) {
				for(int j = Math.max(0,nextY-6);j<Math.min(m_height-1,nextY+6);j++) {
					m_contour[i][j] = -depth;
				}
			}*/
			double choice = m_random.nextGaussian();
			choice = 0;
			if (choice > 0.7)
				direction+=m_random.nextDouble()*Math.PI*2;
			if (choice < -0.7)
				direction-=m_random.nextDouble()*Math.PI*2;

			double dx = Math.cos(direction);
			double dy = Math.sin(direction);
			nextX += Math.ceil(Math.abs(dx)) * ((dx < 0) ? -1 : 1);
			nextY += Math.ceil(Math.abs(dy)) * ((dy < 0) ? -1 : 1);
		}
	}

	public void simulateOceanFlood() {
		if (m_width < 2 || m_height < 2) { return; }

		LinkedList<Point> toFlood = new LinkedList<Point>();
		int oceanLevel = m_depth / 2 - 1;

		for (int x = 0; x < m_width; x++) {
			if (m_blocks[x][0][oceanLevel] == BlockConstants.AIR) {
				floodBlock(x, 0, oceanLevel);
				floodBlock(x, m_height - 1, oceanLevel);
				if (m_blocks[x][1][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point(x, 1));
				} else if (m_blocks[x][m_height - 1][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point(x, m_height - 2));
				}
			}
		}

		if (m_blocks[1][0][oceanLevel] == BlockConstants.AIR) {
			floodBlock(1, 0, oceanLevel);
		}

		if (m_blocks[m_width - 2][0][oceanLevel] == BlockConstants.AIR) {
			floodBlock(m_width - 2, 0, oceanLevel);
		}

		if (m_blocks[1][m_height - 2][oceanLevel] == BlockConstants.AIR) {
			floodBlock(1, m_height - 2, oceanLevel);
		}

		if (m_blocks[m_width - 2][m_height - 2][oceanLevel] == BlockConstants.AIR) {
			floodBlock(m_width - 2, m_height - 2, oceanLevel);
		}

		for (int y = 2; y < m_height - 2; y++) {
			if (m_blocks[0][y][oceanLevel] == BlockConstants.AIR) {
				floodBlock(0, y, oceanLevel);
				floodBlock(m_width - 1, y, oceanLevel);
				if (m_blocks[1][y][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point(1, y));
				} else if (m_blocks[m_width - 2][y][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point(m_width - 2, y));
				}
			}
		}

		while (toFlood.size() > 0) {
			Point p = toFlood.removeFirst();
			if (m_blocks[(int)(p.getX())][(int)(p.getY())][oceanLevel] != BlockConstants.WATER) {

				floodBlock((int)(p.getX()), (int)(p.getY()), oceanLevel);

				if (p.getX() > 0 && m_blocks[(int)(p.getX() - 1)][(int)(p.getY())][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point((int)(p.getX() - 1), (int)(p.getY())));
				}
				
				if (p.getX() < m_width -1 && m_blocks[(int)(p.getX() + 1)][(int)(p.getY())][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point((int)(p.getX() + 1), (int)(p.getY())));
				}
				
				if (p.getY() > 0 && m_blocks[(int)(p.getX())][(int)(p.getY() - 1)][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point((int)(p.getX()), (int)(p.getY() - 1)));
				}
				
				if (p.getY() < m_height -1 && m_blocks[(int)(p.getX())][(int)(p.getY() + 1)][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point((int)(p.getX()), (int)(p.getY() + 1)));
				}
			}
		}
	}

	private void floodBlock(int x, int y, int oceanLevel) {
		for (int z = oceanLevel; true; z--) {
			if (z < 0) { break; }
				if (m_blocks[x][y][z] == BlockConstants.AIR) {
					m_blocks[x][y][z] = BlockConstants.WATER;
				} else if (m_blocks[x][y][z + 1] == BlockConstants.WATER && (m_blocks[x][y][z] == BlockConstants.DIRT || m_blocks[x][y][z] == BlockConstants.GRASS)) {
					m_blocks[x][y][z] = BlockConstants.SAND;
					break;
				}
			}
	}
}

