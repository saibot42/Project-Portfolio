function make2DArray(cols, rows) {
    let arr = new Array(cols);
    for (let i = 0; i < arr.length; i++) {
        arr[i] = new Array(rows);
        for (let j = 0; j < arr[i].length; j++) {
            arr[i][j] = 0;
        }
    }
    return arr;
}

let grid;
let scl = 10;
let cols, rows;


function setup() {
    createCanvas(950, 950)
    background(169);
    cols = width / scl;
    rows = height /scl;
    grid = make2DArray(cols, rows);
    for (let i = 0; i < cols; i++){
        for (let j = 0; j < rows; j++) {
            grid[i][j] = 0;
        }
    }

    grid[20][10] = 1;
}

function draw() {
    background(0)
    for (let i = 0; i < cols; i++){
        for (let j = 0; j < rows; j++) {
           noStroke();
           fill(0);
           if (grid[i][j] > 0) {
            fill(grid[i][j], 255, 255);
            let x = i * scl;
            let y = j * scl;
            square(x, y, scl);
            }
        }
    }
    let nextGrid = checkGrid();
    grid = nextGrid;
    //drawSand();
}

function checkGrid(){ 
    let nextGrid = make2DArray(cols, rows);
    for (let i = 0; i < cols; i++){
        for (let j = 0; j < rows; j++) {
            let state = grid[i][j];
            if(state == 1) {
                let below = grid[i][j + 1];
                let belowLeft = grid[i - 1][j + 1];
                let belowRight = grid[i + 1][j + 1];
                if (below == 0 && j < rows - 1) {
                    nextGrid[i][j] = 0;
                    nextGrid[i][j+1] = 1;
                } else if (belowLeft == 0 && j < rows - 1) {
                    nextGrid[i][j] = 0;
                    nextGrid[i - 1][j+1] = 1
                } else if (belowRight == 0 && j < rows - 1) {
                    nextGrid[i][j] = 0;
                    nextGrid[i + 1][j+1] = 1
                } else {
                    nextGrid[i][j] = 1;                    
             }
            }
        }
    }
    return nextGrid;
}

function mouseDragged() {
    let col = floor(mouseX / scl);
    let row = floor(mouseY / scl);
    
    grid[col][row] = 1;
}