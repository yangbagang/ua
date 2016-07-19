package com.ybg.rp.ua.partner

class PartnerUserOperationLog {

    static belongsTo = [user: PartnerUserInfo]

    static constraints = {
    }

    /** 操作时间 */
    Date operationTime
    /** 操作类型 */
    Short operationType    //0开空柜门 1 开格子柜门 2补货 3 设置价格 4 设置数量 5调换位置
    /** 操作描述 */
    String operationDesc
    /** 设备ID */
    Long deviceId
    /** 轨道编号 */
    String orbitalNo
    /** 操作的ID */
    Long targetId
    /** 操作的数量 */
    Integer targetNum
    /** 操作的结果 */
    Short result
}
