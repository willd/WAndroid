load los.txt;
x = los(:,2);
x = x+1;
y = los(:,1);
y = -y;
f=ezfit(x,y,'log');
plot(x,y,'bo'); hold on;
showfit(f,'lincolor','red');

xlabel('Meters');
ylabel('RSSI values');

title('RSSI values per meter');