package com.happypill.application.util;

import java.util.random.RandomGenerator;

public final class SnowflakeUtil {

    private static final int NODE_ID_BITS    = 10;
    private static final int SEQUENCE_BITS   = 12;

    private static final long MAX_NODE_ID    = (1L << NODE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE   = (1L << SEQUENCE_BITS) - 1;

    // UTC = 2024-01-01T00:00:00Z
    private static final long START_TIME_MILLIS = 1704067200000L;

    // JVM 시작 시 한 번만 결정되는 node ID
    private static final long NODE_ID;

    // 내부 상태
    private static long lastTimeMillis = START_TIME_MILLIS;
    private static long sequence       = 0L;

    static {
        // 환경변수나 설정 없이 랜덤 노드 ID 할당
        NODE_ID = RandomGenerator.getDefault().nextLong(MAX_NODE_ID + 1);
    }

    private SnowflakeUtil() { /* 인스턴스 생성 금지 */ }

    /**
     * 다음 유일한 64-bit ID 생성
     */
    public static synchronized long nextId() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < lastTimeMillis) {
            throw new IllegalStateException(
                    "Clock moved backwards. Refusing to generate id for " +
                            (lastTimeMillis - currentTime) + " ms");
        }
        if (currentTime == lastTimeMillis) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTime = waitNextMillis(currentTime);
            }
        } else {
            sequence = 0L;
        }
        lastTimeMillis = currentTime;

        return ((currentTime - START_TIME_MILLIS) << (NODE_ID_BITS + SEQUENCE_BITS))
                | (NODE_ID << SEQUENCE_BITS)
                | sequence;
    }

    private static long waitNextMillis(long lastTs) {
        long ts = System.currentTimeMillis();
        while (ts <= lastTs) {
            ts = System.currentTimeMillis();
        }
        return ts;
    }
}

