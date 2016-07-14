package com.ybg.rp.ua.device

class VendMachineInfoErrorInfo {

    static belongsTo = [vendMachine: VendMachineInfo]

    static constraints = {
        fixTime nullable: true
    }

    /** 轨道编号*/
    String orbitalNo

    /** 创建时间*/
    Date createTime

    /** 更新时间*/
    Date fixTime

    /**
     * 错误信息
     */
    String errorInfo

    /** 修复状态0：修复，1：未修复*/
    Short status = 0

}
