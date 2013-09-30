#include "mainwindow.h"

#include <QtCore/QCoreApplication>
#include <QtCore/QDebug>

#include <QApplication>
#include <QtSerialPort/QSerialPort>
#include <QtSerialPort/QSerialPortInfo>
QT_USE_NAMESPACE
int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.show();

    foreach (const QSerialPortInfo &info, QSerialPortInfo::availablePorts()) {
          qDebug() << "Name        : " << info.portName();
          qDebug() << "Description : " << info.description();
          qDebug() << "Manufacturer: " << info.manufacturer();
          qDebug() << "Vendor Id: " << info.productIdentifier();

          // Example use QSerialPort
          QSerialPort serial;
          serial.setPort(info);
          if (serial.open(QIODevice::ReadWrite))
              serial.close();
      }

    return a.exec();
}
