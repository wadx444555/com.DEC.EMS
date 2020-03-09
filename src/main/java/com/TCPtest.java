package com;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;

public class TCPtest {
    public static void main(String[] args) {

            try {
                // 设置主机TCP参数
                TcpParameters tcpParameters = new TcpParameters();

                // 设置TCP的ip地址
                InetAddress adress = InetAddress.getByName("127.0.0.1");

                // TCP参数设置ip地址
                // tcpParameters.setHost(InetAddress.getLocalHost());
                tcpParameters.setHost(adress);

                // TCP设置长连接
                tcpParameters.setKeepAlive(true);
                // TCP设置端口，这里设置是默认端口502
                tcpParameters.setPort(711);

                // 创建一个主机
                ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
                Modbus.setAutoIncrementTransactionId(true);

                int slaveId = 1;//从机地址
                int offset = 2000;//寄存器读取开始地址
                int quantity = 10;//读取的寄存器数量


                try {
                    if (!master.isConnected()) {
                        master.connect();// 开启连接
                    }

                    // 读取对应从机的数据，readInputRegisters读取的写寄存器，功能码04
                    int[] registerValues = master.readInputRegisters(slaveId, offset, quantity);
                    int[] readHoldingRegister=master.readHoldingRegisters(1,2000,2);
                    //boolean[] r = master.readDiscreteInputs(1,100,10);

                    // 控制台输出
                    for (int value : registerValues) {
                        System.out.println("Address: " + offset++ + ", Value: " + value);
                    }
                    for (int value : readHoldingRegister) {
                        System.out.println("Address: " + offset++ + ", Value: " + value);
                    }
//                    for (boolean value : r) {
//                        System.out.println("Address: " + offset++ + ", Value: " + value);
//                    }

                } catch (ModbusProtocolException e) {
                    e.printStackTrace();
                } catch (ModbusNumberException e) {
                    e.printStackTrace();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        master.disconnect();
                    } catch (ModbusIOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
