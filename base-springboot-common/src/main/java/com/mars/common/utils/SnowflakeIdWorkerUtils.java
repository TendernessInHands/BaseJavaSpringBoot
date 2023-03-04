//package com.mars.common.utils;
//
/**
 * 生成ID
 * @author mars
 */
//public class SnowflakeIdWorkerUtils {
//
//    //起始的时间戳
//    private final static long twepoch = 1557825652094L;
//
//    //每一部分占用的位数
//    private final static long workerIdBits = 5L;
//    private final static long datacenterIdBits = 5L;
//    private final static long sequenceBits = 12L;
//
//    //每一部分的最大值
//    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
//    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
//    private final static long maxSequence = -1L ^ (-1L << sequenceBits);
//
//    //每一部分向左的位移
//    private final static long workerIdShift = sequenceBits;
//    private final static long datacenterIdShift = sequenceBits + workerIdBits;
//    private final static long timestampShift = sequenceBits + workerIdBits + datacenterIdBits;
//
//    private long datacenterId; // 数据中心ID
//    private long workerId; // 机器ID
//    private long sequence = 0L; // 序列号
//    private long lastTimestamp = -1L; // 上一次时间戳
//
//    public SnowflakeIdWorkerUtils(long workerId, long datacenterId) {
//        if (workerId > maxWorkerId || workerId < 0) {
//            throw new IllegalArgumentException(
//                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
//        }
//        if (datacenterId > maxDatacenterId || datacenterId < 0) {
//            throw new IllegalArgumentException(
//                    String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
//        }
//        this.workerId = workerId;
//        this.datacenterId = datacenterId;
//    }
//
//    public synchronized long nextId() {
//        long timestamp = timeGen();
//        if (timestamp < lastTimestamp) {
//            throw new RuntimeException(String.format(
//                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
//        }
//        if (timestamp == lastTimestamp) {
//            sequence = (sequence + 1) & maxSequence;
//            if (sequence == 0L) {
//                timestamp = tilNextMillis();
//            }
//        } else {
//            sequence = 0L;
//        }
//        lastTimestamp = timestamp;
//
//        return (timestamp - twepoch) << timestampShift // 时间戳部分
//                | datacenterId << datacenterIdShift // 数据中心部分
//                | workerId << workerIdShift // 机器标识部分
//                | sequence; // 序列号部分
//    }
//
//    private long tilNextMillis() {
//        long timestamp = timeGen();
//        while (timestamp <= lastTimestamp) {
//            timestamp = timeGen();
//        }
//        return timestamp;
//    }
//
//    private long timeGen() {
//        return System.currentTimeMillis();
//    }
//
//    /** 测试 */
//    public static String getSnowId() {
//        SnowflakeIdWorkerUtils idWorker = new SnowflakeIdWorkerUtils(0, 0);
//        String id = String.valueOf(idWorker.nextId());
//        return id;
//    }
//
//    public static void main(String[] args) {
//        for (int i = 0; i < 1000; i++) {
//            String snowId = getSnowId();
//
//        }
//    }
//
//}
