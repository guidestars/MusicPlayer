package com.xu.musicplayer.fft;

/*************************************************************************
 * Compilation: javac FFT.java Execution: java FFT N Dependencies: Complex.java
 *
 * Compute the FFT and inverse FFT of a length N complex sequence. Bare bones
 * implementation that runs in O(N log N) time. Our goal is to optimize the
 * clarity of the code, rather than performance.
 *
 * Limitations ----------- - assumes N is a power of 2
 *
 * - not the most memory efficient algorithm (because it uses an object type for
 * representing complex numbers and because it re-allocates memory for the
 * subarray, instead of doing in-place or reusing a single temporary array)
 *
 *************************************************************************/
public class FFT {

    // compute the FFT of x[], assuming its length is a power of 2
    public static Complex[] fft(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1)
            return new Complex[]{x[0]};

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) {
            throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd = even; // reuse the array
        for (int k = 0; k < N / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    // compute the inverse FFT of x[], assuming its length is a power of 2
    public static Complex[] ifft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].scale(1.0 / N);
        }

        return y;

    }

    // compute the circular convolution of x and y
    public static Complex[] cconvolve(Complex[] x, Complex[] y) {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.length != y.length) {
            throw new RuntimeException("Dimensions don't agree");
        }

        int N = x.length;

        // compute FFT of each sequence����ֵ
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply����ֵ�˷�
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT����ֵ
        return ifft(c);
    }

    // compute the linear convolution of x and y
    public static Complex[] convolve(Complex[] x, Complex[] y) {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2 * x.length];// 2n�����磬�߽�ϵ��Ϊ0.
        System.arraycopy(x, 0, a, 0, x.length);
        for (int i = x.length; i < 2 * x.length; i++)
            a[i] = ZERO;

        Complex[] b = new Complex[2 * y.length];
        System.arraycopy(y, 0, b, 0, y.length);
        for (int i = y.length; i < 2 * y.length; i++)
            b[i] = ZERO;

        return cconvolve(a, b);
    }

    // display an array of Complex numbers to standard output
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        int complexLength = x.length;
        for (int i = 0; i < complexLength; i++) {
            // �������
            // System.out.println(x[i]);
            // �����ֵ��Ҫ * 2 / length
            System.out.println(x[i].abs() * 2 / complexLength);
        }
        System.out.println();
    }

    /**
     * ���������������2���ݴη����
     *
     * @param data
     * @return
     */
    public static Double[] pow2DoubleArr(Double[] data) {

        // ����������
        Double[] newData = null;

        int dataLength = data.length;

        int sumNum = 2;
        while (sumNum < dataLength) {
            sumNum = sumNum * 2;
        }
        int addLength = sumNum - dataLength;

        if (addLength != 0) {
            newData = new Double[sumNum];
            System.arraycopy(data, 0, newData, 0, dataLength);
            for (int i = dataLength; i < sumNum; i++) {
                newData[i] = 0d;
            }
        } else {
            newData = data;
        }

        return newData;

    }

    /**
     * ȥƫ����
     *
     * @param originalArr ԭ����
     * @return Ŀ������
     */
    public static Double[] deskew(Double[] originalArr) {
        // ���˲���ȷ�Ĳ���
        if (originalArr == null || originalArr.length <= 0) {
            return null;
        }

        // ����Ŀ������
        Double[] resArr = new Double[originalArr.length];

        // �������ܺ�
        Double sum = 0D;
        for (int i = 0; i < originalArr.length; i++) {
            sum += originalArr[i];
        }

        // ������ƽ��ֵ
        Double aver = sum / originalArr.length;

        // ȥ��ƫ��ֵ
        for (int i = 0; i < originalArr.length; i++) {
            resArr[i] = originalArr[i] - aver;
        }

        return resArr;
    }


    public static void main(String[] args) {
        // int N = Integer.parseInt(args[0]);
        Double[] data = {-0.35668879080953375, -0.6118094913035987, 0.8534269560320435, -0.6699697478438837, 0.35425500561437717,
                0.8910250650549392, -0.025718699518642918, 0.07649691490732002};

        // ȥ��ƫ����
        data = deskew(data);
        // ����Ϊ2���ݴη�
        data = pow2DoubleArr(data);

        int N = data.length;
        System.out.println(N + "����N������....");
        Complex[] x = new Complex[N];
        // original data
        for (int i = 0; i < N; i++) {
            // x[i] = new Complex(i, 0);
            // x[i] = new Complex(-2 * Math.random() + 1, 0);
            x[i] = new Complex(data[i], 0);
        }

        show(x, "x");

        // FFT of original data

        Complex[] y = fft(x);
        show(y, "y = fft(x)");

        // take inverse FFT
        Complex[] z = ifft(y);
        show(z, "z = ifft(y)");

        // circular convolution of x with itself
        Complex[] c = cconvolve(x, x);
        show(c, "c = cconvolve(x, x)");

        // linear convolution of x with itself
        Complex[] d = convolve(x, x);
        show(d, "d = convolve(x, x)");
    }
}