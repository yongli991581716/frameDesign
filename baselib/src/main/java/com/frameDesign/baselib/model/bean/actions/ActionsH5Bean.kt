package com.frameDesign.baselib.model.bean.actions

import com.frameDesign.baselib.model.bean.BaseBean

/**
 * Created by gaoyang on 2017/12/21.
 */
class ActionsH5Bean : BaseBean() {

    var actions: List<ActionsH5BeanItem> = mutableListOf()

    var shareDomains: List<ActionsH5ShareBean> = mutableListOf()

}