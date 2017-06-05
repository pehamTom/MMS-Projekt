package filters.convolution;

public abstract class KernelFunction implements WindowFunction{
    protected final float[][] kernel;
    protected final int kernelHeight;
    protected final int kernelWidth;
    protected final int kernelHalfHeight;
    protected final int kernelHalfWidth;

    public KernelFunction(float[][] kernel) {
        this.kernel = kernel;
        kernelHeight = kernel.length;
        kernelWidth = kernel[0].length;
        kernelHalfHeight = kernelHeight/2;
        kernelHalfWidth = kernelWidth/2;
    }
}
