import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class Matrix {

    static final BigInteger ZERO_BIG_INTEGER = BigInteger.valueOf(0);
    static final BigInteger ONE_BIG_INTEGER = BigInteger.valueOf(1);
    static final BigInteger NEGATIVE_ONE_BIG_INTEGER = BigInteger.valueOf(-1);

    int rows;
    int columns;
    Fraction[][] data;

    // constructor that builds a zero matrix
    Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new Fraction[rows][columns];

        // Makes zero matrix
        for(int row = 0; row < this.rows; row++) {
            for(int col = 0; col < this.columns; col++) {
                data[row][col] = new Fraction(ZERO_BIG_INTEGER, ONE_BIG_INTEGER);
            }
        }
    }

    // constructor that takes an array of Fractions (all entries)
    Matrix(int rows, int columns, Fraction[] entries) {
        this.rows = rows;
        this.columns = columns;
        this.data = new Fraction[rows][columns];

        if( (rows * columns) != entries.length ) {
            throw new ArithmeticException("Matrix has " + (rows*columns) + " entries and " + entries.length + " entries were supplied.");
        }

        int inputEntry = 0;
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                this.data[row][col] = entries[inputEntry];
                inputEntry++;
            }
        }
    }

    // returns a random integer from 0 to range
    private int getRandomNumber(int range) {
        return new Random().nextInt(range);
    }

    public Matrix makeIdentityMatrix() {
        // takes a matrix and, if square, returns an identity matrix of same size
        Matrix identityMatrix = new Matrix(this.rows, this.columns);
        if(this.rows == this.columns) {
            for (int row = 0; row < this.rows; row++) {
                for (int col = 0; col < this.columns; col++) {
                    if(row == col) {
                        identityMatrix.data[row][col] = new Fraction(1, 1);
                    }
                }
            }
        }
        return identityMatrix;
    }

    // sets all entries in a matrix instance to a random number in
    // in range 0, randRange
    public void randomize(int randRange) {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                int num = getRandomNumber(randRange);
                BigInteger randBigInteger = new BigInteger(Integer.toString(num));
                this.data[row][col] = new Fraction(randBigInteger, ONE_BIG_INTEGER);
            }
        }
    }

    public void showSelf() {
        String[][] stringRep = new String[this.rows][this.columns];
        // place string representation of fractions in stringRep
        for(int row = 0; row < this.rows; row++) {
            for(int col = 0; col < this.columns; col++) {
                stringRep[row][col] = this.data[row][col].getString();
            }
        }
        // print rows
        for(int row = 0; row < this.rows; row++) {
            System.out.println(Arrays.toString(stringRep[row]));
        }
    }

    // returns a Matrix instance that is the sum of the two addends
    public Matrix add(Matrix addend) {
        if (this.rows != addend.rows || this.columns != addend.columns) {
            throw new ArithmeticException("Matrices must be same size to perform addition.");
        }
        Matrix sum = new Matrix(this.rows, this.columns);
        for (int row = 0; row < sum.rows; row++) {
            for (int col = 0; col < sum.columns; col++) {
                sum.data[row][col] = this.data[row][col].add(addend.data[row][col]);
            }
        }
        
        return sum;
    }

    // returns a Matrix instance that is the difference of the two matrices
    public Matrix subtract(Matrix addend) {
        if (this.rows != addend.rows || this.columns != addend.columns) {
            throw new ArithmeticException("Matrices must be same size to perform addition.");
        }
        Matrix difference = new Matrix(this.rows, this.columns);
        for (int row = 0; row < difference.rows; row++) {
            for (int col = 0; col < difference.columns; col++) {
                difference.data[row][col] = this.data[row][col].subtract(addend.data[row][col]);
            }
        }

        return difference;
    }

    // returns the product of two Matrix instances
    public Matrix multiply(Matrix multiplicand) {
        if (this.columns != multiplicand.rows) {
            throw new ArithmeticException("this.columns = " + this.columns + " | multiplicand.rows = " + multiplicand.rows + " - These two must be equal.");
        }

        Matrix product = new Matrix(this.rows, multiplicand.columns);
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < multiplicand.columns; col++) {
                // get row data from 1st matrix and column data from second matrix
                Fraction[] rowData = this.data[row];
                Fraction[] colData = multiplicand.getColumn(col);
                Fraction result = new Fraction(ZERO_BIG_INTEGER, ONE_BIG_INTEGER);
                // for each item in the arrays, get the product of each respective entry then sum them.
                for (int entry = 0; entry < rowData.length; entry++) {
                    result = result.add( rowData[entry].multiply(colData[entry]) );
                }
                product.data[row][col] = result;
            }
        }

        return product;
    }

    // returns a Matrix instance that is scaled to the scalar
    public Matrix scale(Fraction scalarFraction) {
        Matrix scaledMatrix = new Matrix(this.rows, this.columns);
        
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                scaledMatrix.data[row][col] = this.data[row][col].multiply(scalarFraction);
            }
        }

        return scaledMatrix;
    }

    // returns an array of Fractions that corresponds to the entries in the specified column
    public Fraction[] getColumn(int column) { 
        Fraction[] resultArray = new Fraction[this.rows];

        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if (col == column) {
                    resultArray[row] = this.data[row][col];
                }
            }
        }

        return resultArray;
    }

    // returns a Matrix instance that is the transpose of the given instance
    public Matrix transpose() {
        Matrix transpose = new Matrix(this.rows, this.columns);
        
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                transpose.data[row][col] = this.getColumn(row)[col];
            }
        }

        return transpose;
    }

    // returns a Matrix instance that is the minor matrix of the specified entry
    public Matrix minorMatrix(int entryRow, int entryCol) {
        if(entryRow >= this.rows || entryCol >= this.columns) {
            throw new ArithmeticException("Error: rows or columns out of range. entryRow and entryCol must be smaller than this.rows and this.columns");
        }
        Matrix minor = new Matrix(this.rows - 1, this.columns - 1);
        Fraction[] values = new Fraction[(minor.rows * minor.columns)];

        // fill up values with the entries in the matrix that aren't in the row and column
        int counter = 0;
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if (row != entryRow && col != entryCol) {
                    values[counter] = this.data[row][col];
                    counter++;
                }
            }
        }

        // place the acquired values into the minor matrix
        counter = 0;
        for (int row = 0; row < minor.rows; row++) {
            for (int col = 0; col < minor.columns; col++) {
                minor.data[row][col] = values[counter];
                counter++;
            }
        }
        return minor;
    }

    // returns a Fraction that is the determinant of the given matrix using cofactor expansion
    public Fraction determinant() {
        if (this.rows != this.columns) {
            throw new ArithmeticException("Rows must equal columns to get determinant");
        }
        Fraction result = new Fraction(ZERO_BIG_INTEGER, ONE_BIG_INTEGER);
        if(this.rows == 2){
            result =  (this.data[0][0].multiply(this.data[1][1])).subtract(this.data[0][1].multiply(this.data[1][0]));
        }
        // implement rule of Sarrus for 3x3 matrix
        if(this.rows >= 3) {
            for (int row = 0; row < this.rows; row++) {
                if (row % 2 == 0) {
                    result = result.add(this.data[row][0].multiply(this.minorMatrix(row, 0).determinant()));
                } else {
                    result = result.subtract(this.data[row][0].multiply(this.minorMatrix(row, 0).determinant()));
                }
            }
        }
        return result;
    }

    // takes operatorRow, multiplies it by the multiplier and adds it do operatingRow
    public Matrix rowOperation(int operatorRowPos, int operatingRowPos, Fraction multiplier) {
        Matrix resultMatrix = new Matrix(this.rows, this.columns);
        // use arrays.copyOf to not pass the reference and not change the original array
        resultMatrix.data = Arrays.copyOf(this.data, this.data.length);
        
        Fraction[] operatorRow = Arrays.copyOf(this.data[operatorRowPos], this.data[operatorRowPos].length);
        Fraction[] operatingRow = Arrays.copyOf(this.data[operatingRowPos], this.data[operatingRowPos].length);

        // multiplies all entries in operatorRow array by the multiplier
        for (int entry = 0; entry < operatorRow.length; entry++) {
            operatorRow[entry] = operatorRow[entry].multiply(multiplier);
        }
        // adds each entry in operatorRow to operatingRow
        for (int entry = 0; entry < operatingRow.length; entry++) {
            operatingRow[entry] = operatingRow[entry].add(operatorRow[entry]);
        }
        // insert operatingRow into resultMatrix at operatingRowPos
        resultMatrix.data[operatingRowPos] = operatingRow;

        return resultMatrix;
    }

    // returns the value of entry at position
    public Fraction getEntry(int row, int col) {
        return this.data[row][col];
    }

    // find row after rowPosition with nonzero entry at colPosition, returns the position of that row (int) returns -1 if no row 
    // with nonzero entry at colPosition is found. Helper funciton for makeUsable().
    public int findUsableRow(int rowPosition, int colPosition) {

        for (int row = rowPosition + 1; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if (colPosition == col && !this.data[row][col].getNumerator().equals(ZERO_BIG_INTEGER)) {
                    return row;
                }
            }
        }
        return -1;
    }

    // takes rowPosition and colPosition and, if it finds a useable row below it (a row where the entry at the same column is nonzero...),
    // adds it to row at rowPosition, then returns the matrix. Helper function for makeUpperTriangular().
    public Matrix makeUsable(int rowPosition, int colPosition) {
        Matrix usableMatrix = new Matrix(this.rows, this.columns);
        usableMatrix.data = Arrays.copyOf(this.data, this.data.length);
        int usableRowPosition = this.findUsableRow(rowPosition, colPosition);
        if (usableRowPosition >= 0) {
            // takes row at usableRowPosition and adds it to rowPosition (use ONE_BIG_INTEGER because we're adding itself x 1)
            usableMatrix = this.rowOperation(usableRowPosition, rowPosition, new Fraction(ONE_BIG_INTEGER, ONE_BIG_INTEGER));
        }
        return usableMatrix;
    }

    // does a rowOperation on each row below rowPosition. The goal is to make every entry under rowPosition @ colPosition = 0, using the
    // entry @ rowPosition & colPosition. Helper function for makeUpperTriangular().
    public Matrix subtractDown(int rowPosition, int colPosition) {
        Matrix workingMatrix = new Matrix(this.rows, this.columns);
        workingMatrix.data = Arrays.copyOf(this.data, this.data.length);
        
        for (int rowBelow = rowPosition + 1; rowBelow < workingMatrix.rows; rowBelow++) {
            // if denominator will be 0, return workingMatrix with no changes
            if(workingMatrix.data[rowPosition][colPosition].getNumerator().equals(ZERO_BIG_INTEGER)) {
                return workingMatrix;
            }
            // get right ratio
            Fraction mult = new Fraction(workingMatrix.data[rowBelow][colPosition], workingMatrix.data[rowPosition][colPosition]);
            // multiply it by -1 because we're subtracting
            mult = mult.multiply(new Fraction(NEGATIVE_ONE_BIG_INTEGER, ONE_BIG_INTEGER));
            // perform row operation
            workingMatrix = workingMatrix.rowOperation(rowPosition, rowBelow, mult);
        }

        return workingMatrix;
    }

    // uses subtractDown down the main diagonal of the array to produce an upper triangular matrix. Helper function for fastDeterminant()
    public Matrix makeUpperTriangular() {
        Matrix workingMatrix = new Matrix(this.rows, this.columns);
        workingMatrix.data = Arrays.copyOf(this.data, this.data.length);
        // make the matrix triangular using only elementary row operations (to avoid dealing with factorization)
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if(row == col) {
                    if(workingMatrix.data[row][col].getNumerator().equals(ZERO_BIG_INTEGER)) {
                        workingMatrix = workingMatrix.makeUsable(row, col);
                    }
                    workingMatrix = workingMatrix.subtractDown(row, col);
                }
            }
        }
        return workingMatrix;
    }

    public Fraction fastDeterminant() {
        Matrix workingMatrix = new Matrix(this.rows, this.columns);
        workingMatrix.data = Arrays.copyOf(this.data, this.data.length);
        workingMatrix = workingMatrix.makeUpperTriangular();
        
        Fraction result = new Fraction(ONE_BIG_INTEGER, ONE_BIG_INTEGER);
        for (int row = 0; row < workingMatrix.rows; row++) {
            for (int col = 0; col < workingMatrix.rows; col++) {
                if (row == col) {
                    result = result.multiply(workingMatrix.data[row][col]);
                }
            }
        }
        return result;
    }

    public Matrix inverse() {
        // returns the inverse of a matrix
        Fraction originalDeterminant = this.fastDeterminant();
        Matrix matrixOfMinors = new Matrix(this.columns, this.rows);
        // get matrix of minors
        for (int row = 0; row < matrixOfMinors.rows; row++) {
            for (int col = 0; col < matrixOfMinors.columns; col++) {
                matrixOfMinors.data[row][col] = this.minorMatrix(row, col).fastDeterminant();
                
            }
        }
        // convert to matrix of cofactors
        for (int row = 0; row < matrixOfMinors.rows; row++) {
            for (int col = 0; col < matrixOfMinors.columns; col++) {
                if( (col % 2 != 0 && row % 2 == 0) || (col % 2 == 0 && row % 2 != 0) ) {
                    matrixOfMinors.data[row][col] = matrixOfMinors.data[row][col].multiply(new Fraction(NEGATIVE_ONE_BIG_INTEGER, ONE_BIG_INTEGER));
                }
            }
        }
        // transpose
        matrixOfMinors = matrixOfMinors.transpose();

        return matrixOfMinors.scale(new Fraction(new Fraction(ONE_BIG_INTEGER, ONE_BIG_INTEGER), originalDeterminant));

    }

    // returns true if matrices contain the same data, false otherwise
    public boolean isEqualTo(Matrix otherMatrix) {
        if(this.rows == otherMatrix.rows && this.columns == otherMatrix.columns) {
            for (int row = 0; row < this.rows; row++) {
                for (int col = 0; col < this.columns; col++) {
                    if(!this.data[row][col].isEqualTo(otherMatrix.data[row][col])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // returns true if matrix is orthogonal, false otherwise
    public boolean isOrthogonal() {
        Matrix product = this.multiply(this.transpose());
        Matrix identity = new Matrix(this.rows, this.columns).makeIdentityMatrix();
        
        return product.isEqualTo(identity);
        
    }
}
