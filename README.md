# ClusterParser

Mindera Graduate Program Adjacent Cells Challenge - https://tinyurl.com/mindera-graduates-grid 

### A program that is capable of identifying all the groups of adjacent cells in a grid.

The grid consists of a 2-dimensional array of 0 (zeroes) and 1 (ones). The output of the program should consist of a sequence of multi-dimensional arrays each containing the list of points that make up a group of adjacent cells.

Rules:

Cells are considered adjacent if they both contain a 1 (one) and are next to each other horizontally or vertically (not diagonally).
Groups containing a single cell should not be considered.

The order of points or groups outputted by the program is not relevant


Example

For a grid input of:

```[[0,0,0,1,0,0,1,1],

 [0,0,1,1,1,0,1,1],
 
 [0,0,0,0,0,0,1,0],
 
 [0,0,0,1,0,0,1,1],
 
 [0,0,0,1,0,0,1,1]]
 ```
 
 
Expected output:

```[ [0,3], [1,2], [1,3], [1,4] ]

[ [0,6], [0,7], [1,6], [1,7], [2,6], [3,6], [3,7], [4,6], [4,7] ]

[ [3, 3], [4,3] ]
```


Test input data files:

https://tinyurl.com/mindera-grid-100x100-tgz

https://tinyurl.com/mindera-grid-1000x1000-tgz

https://tinyurl.com/mindera-grid-10000x10000-tgz

https://tinyurl.com/mindera-grid-20000x20000-tgz

