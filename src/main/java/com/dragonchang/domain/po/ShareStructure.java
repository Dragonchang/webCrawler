package com.dragonchang.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: webcrawler
 * @description: 公司持有其它公司股份信息
 * @author: zhangfl
 * @create: 2021-02-18 11:42
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_share_structure")
public class ShareStructure {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
}
