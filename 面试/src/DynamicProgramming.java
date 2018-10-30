package com.ambow;

import java.util.ArrayList;
import java.util.List;

/**
 * 找出最佳路径
 * @author pengjieran
 *
 */
public class DynamicProgramming {

	// 定义best(i,j) 和 M N
	private int[][] best = null;
	private int M = 0;
	private int N = 0;

	// 定义方向常量
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	// 存储最好路径
	private List<Integer> bestPath = null;

	// 计算best(i,j)
	private void calcDp(int[][] matrix) {
		// 初始化
		M = matrix.length;
		N = matrix[0].length;
		best = new int[M][N];
		// 计算
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				// 边界
				if (i == 0 && j == 0) {
					best[i][j] = matrix[i][j];
				} else if (i == 0) {
					best[i][j] = best[i][j - 1] + matrix[i][j];
				} else if (j == 0) {
					best[i][j] = best[i - 1][j] + matrix[i][j];
				} else {
					// 状态转移
					best[i][j] = Math.max(best[i - 1][j], best[i][j - 1]) + matrix[i][j];
				}
			}
		}
	}

	// 获取最大值
	public int getMaxAward(int[][] matrix) {
		// 计算状态
		calcDp(matrix);
		// 计算最佳路径
		calcBestPath();
		// 返回最大值
		return best[matrix.length - 1][matrix[0].length - 1];
	}

	// 计算最佳路径，从后往前
	private void calcBestPath() {
		bestPath = new ArrayList<Integer>();
		// 总共走 M + N - 2 步
		int curX = M - 1;
		int curY = N - 1;
		// 根据best(i,j)计算最佳路径
		for (int i = 0; i < M + N - 2; i++) {
			if (curX == 0) {
				curY--;
				bestPath.add(RIGHT);
			} else if (curY == 0) {
				curX--;
				bestPath.add(DOWN);
			} else {
				if (best[curX - 1][curY] > best[curX][curY - 1]) {
					curX--;
					bestPath.add(DOWN);
				} else {
					curY--;
					bestPath.add(RIGHT);
				}
			}
		}
	}

	// 打印最佳路径
	public void printBestPath() {
		// 倒序打印
		for (int i = bestPath.size() - 1; i >= 0; i--) {
			if (bestPath.get(i) == RIGHT) {
				System.out.print("右 ");
			} else {
				System.out.print("下 ");
			}
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		int[][] matrix1 = {
	            {300, 500, 560, 400, 160},
	            {1000, 100, 200, 340, 690},
	            {600, 500, 500, 460, 320},
	            {300, 400, 250, 210, 760}
	        };

	        int[][] matrix2 = {
	            {300, 500, 2560, 400},
	            {1000, 100, 200, 340},
	            {600, 500, 500, 460},
	            {300, 400, 250, 210},
	            {860, 690, 320, 760}
	        };

	        DynamicProgramming dp = new DynamicProgramming();

	        System.out.println(dp.getMaxAward(matrix1));
	        dp.printBestPath();

	        System.out.println(dp.getMaxAward(matrix2));
	        dp.printBestPath();
	}
}
