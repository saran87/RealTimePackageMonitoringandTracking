close all;
clear all;
%!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!%
% Specify input file path
%!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!%
file = '2013-12-21 11:29:21.csv';

filettl = strrep(file,'_',' ');

data = load(file, 'ascii');
data = strrep(data,',',' ');
disp(data);

% Get number of rows (samples) in the file
Nrows = length(data);
len = 1:Nrows;

%!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!%
% Set the plot values
%!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!%
SAMPLEFREQ = 1600;
freq = -Nrows/2:1:Nrows/2-1;
freq = freq ./ Nrows;
freq = freq .* SAMPLEFREQ;
XMIN = 0;
XMAX = Nrows;
XMINF = freq(Nrows/2);
XMAXF = freq(Nrows);
YMINF = 0;
YMAXF = 200;
figure1_rows = 2;
figure1_cols = 1;
figure2_rows = 3;
figure2_cols = 2;
figure3_rows = 3;
figure3_cols = 2;
figure4_rows = 4;
figure4_cols = 2;



% Read data into arrays
Xdata = data(:,1);
Ydata = data(:,2);
Zdata = data(:,3);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Figure 1
%   * Raw Voltage Plots
%   * G-Force Plots
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Compute G vector
i = 1;
while(i <= Nrows)
    vector(i) = sqrt(Xdata(i)^2 + Ydata(i)^2 + Zdata(i)^2);    
    i = i + 1;
end
vector = vector';

figure;


%-----------------------------------------------------------------------------%
% G-Force Plots
%-----------------------------------------------------------------------------%

% X Y Z G-Force Plot
subplot(figure1_rows,figure1_cols,1);
plot(len,Xdata,len,Ydata,len,Zdata);
title('X, Y, and Z Axes in G Force');
xlabel('Samples');
ylabel('G Force');
leg3 = legend('X Axis', 'Y Axis', 'Z Axis');
set(leg3, 'Location', 'SouthWest');
axis([XMIN XMAX -Inf Inf]);

% Vector G-Force Plot
subplot(figure1_rows,figure1_cols,2);
plot(vector);
title('Vector in G Force');
xlabel('Samples');
ylabel('G Force');
axis([XMIN XMAX -Inf Inf]);


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Figure 3
%   * Hanning Window Plots
%   * FFT with Hanning Window Plots
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Calculations for Hanning Windows and FFTs
hannConst = hann(Nrows, 'periodic');

hannX = Xdata.*hannConst;
hannY = Ydata.*hannConst;
hannZ = Zdata.*hannConst;
hannV = vector.*hannConst;

fftX = (abs(fft(hannX)));
fftY = (abs(fft(hannY)));
fftZ = (abs(fft(hannZ)));
fftV = (abs(fft(hannV)));

fftV = (fftV.^2)/1600
figure;

%-----------------------------------------------------------------------------%
% Hanning Window Plots
%-----------------------------------------------------------------------------%
subplot(figure4_rows,figure4_cols,1);
plot(hannX);
grid;
title('Hanning Window for X Axis');
xlabel('Normalized Frequency');
ylabel('Value (g)');
axis([XMIN XMAX -Inf Inf]);

subplot(figure4_rows,figure4_cols,3);
plot(hannY);
grid;
title('Hanning Window for Y Axis');
xlabel('Normalized Frequency');
ylabel('Value (g))');
axis([XMIN XMAX -Inf Inf]);

subplot(figure4_rows,figure4_cols,5);
plot(hannZ);
grid;
title('Hanning Window for Z Axis');
xlabel('Normalized Frequency');
ylabel('Value (g)');
axis([XMIN XMAX -Inf Inf]);


subplot(figure4_rows,figure4_cols,7);
plot(hannV);
grid;
title('Hanning Window for Vector');
xlabel('Normalized Frequency');
ylabel('Value (g)');
axis([XMIN XMAX -Inf Inf]);

%-----------------------------------------------------------------------------%
% FFT with Hanning Window Plots
%-----------------------------------------------------------------------------%
subplot(figure4_rows,figure4_cols,2);
plot(fftX(1:Nrows/2));
grid;
title('FFT with Hanning Window for X Axis');
xlabel('Normalized Frequency');
ylabel('G^2/Hz');
axis([XMIN XMAX/2 -Inf Inf]);

subplot(figure4_rows,figure4_cols,4);
plot(fftY(1:Nrows/2));
grid;
title('FFT with Hanning Window for Y Axis');
xlabel('Normalized Frequency');
ylabel('G^2/Hz');
axis([XMIN XMAX/2 -Inf Inf]);

subplot(figure4_rows,figure4_cols,6);
plot(fftZ(1:Nrows/2));
grid;
title('FFT with Hanning Window for Z Axis');
xlabel('Normalized Frequency');
ylabel('G^2/Hz');
axis([XMIN XMAX/2 -Inf Inf]);

subplot(figure4_rows,figure4_cols,8);
plot(fftV(3:350));
grid;
title('FFT with Hanning Window for Vector');
xlabel('Normalized Frequency');
ylabel('G^2/Hz');
axis([2 300 -Inf Inf]);


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% General Outputs
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
disp(['Vector G Mean Value: ', num2str(mean(vector)), 'G']);
disp(['Vector xG Mean Value: ', num2str(mean(Xdata)), 'G']);
disp(['Vector yG Mean Value: ', num2str(mean(Ydata)), 'G']);
disp(['Vector zG Mean Value: ', num2str(mean(Zdata)), 'G']);