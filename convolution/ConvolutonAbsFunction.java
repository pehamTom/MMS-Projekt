package filters.convolution;

public class ConvolutonAbsFunction extends ConvolutionFunction {
    public ConvolutonAbsFunction(float[][] kernel) {
        super(kernel);
    }

    @Override
    protected int processValue(int value) {
        return Math.min(255,Math.abs(value));
    }
}
