package com.frameDesign.commonlib.uitls

import android.annotation.SuppressLint
import com.frameDesign.commonlib.CommHelper
import java.util.*
import kotlin.collections.ArrayList

/**
 * 集合工具
 *
 * @author liyong
 * @date  2017/12/21.
 */
@SuppressLint("StaticFieldLeak")
object CollectionUtils {
    private var ctx = CommHelper.mCtx

    /**
     * 找出 某个值在 标签 里面的索引位置
     * @param v V
     * @param lst List<V>
     * @return Int
     */
    fun <V> getIndex(v: V, lst: List<V>): Int {
        for (idx in lst.withIndex()) {
            if (idx.value == v) {
                return idx.index
            }
        }
        return -1
    }

    /**
     * 得到最后1个KEY
     * @param map
     * @param <K>
     * @param <V>
     * @return
    </V></K> */
    fun <K, V> getLastKey(map: Map<K, V>): K? {
        if (!isEmpty(map)) {
            var lastk: K? = null
            for (k in map.keys) {
                lastk = k
            }

            return lastk
        }

        return null
    }

    /**
     * 得到最后一个值
     * @param list
     * @param <T>
     * @return
    </T> */
    fun <T> getLastValue(list: List<T>): T? {
        return if (!isEmpty(list)) {
            list[list.size - 1]
        } else null
    }

    /**
     *
     * @Title: cloneList
     *
     * @Description: Clone a list.
     *
     * @param @param
     * list @param @return @return List<T> @throws
    </T> */
    fun <T> cloneList(list: List<T>): List<T> {
        val newList = ArrayList<T>()
        if (!isEmpty(list)) {
            newList.addAll(list)
        }

        return newList
    }

    /**
     * clone map,通常为了避免原始数据受影响
     * @param map
     * @param <K></K>, V>
     * @return
     */
    fun <K, V> cloneMap(map: Map<K, V>): Map<K, V> {
        val newMap = HashMap<K, V>()

        if (!isEmpty(map)) {
            newMap.putAll(map)
        }

        return newMap
    }

    /**
     *
     * @Title: equals
     *
     * @param @param
     * list1 @param @param list2 @param @return @return
     * boolean @throws
     */
    fun <T> equals(list1: List<T>?, list2: List<T>?): Boolean {
        if (!isEmpty(list1) && !isEmpty(list2)) {
            if (list1!!.size == list2!!.size) {
                var calculate = 0
                for (t1 in list1) {
                    for (t2 in list2) {
                        if (t1 === t2) {
                            calculate++
                            break
                        }
                    }
                }

                if (calculate == list1.size) {
                    return true
                }
            }
        }

        return false
    }

    fun <T> listPop(list: MutableList<T>?) {
        if (!isEmpty(list)) {
            list?.removeAt(list.size - 1)
        }
    }

    fun <T> getSize(list: List<T>?): Int {
        return list?.size ?: 0
    }

    fun <K, V> getSize(map: Map<K, V>?): Int {
        return map?.size ?: 0
    }

    fun <T> getSize(set: Set<T>?): Int {
        return set?.size ?: 0
    }

    fun <T> getSize(array: Array<T>?): Int {
        return array?.size ?: 0
    }

    fun <T> equals(t1: T?, t2: T): Boolean {
        return if (t1 != null) {
            t1 == t2
        } else false

    }

    fun <T : String> equalsIgnoreCase(t1: T?, t2: T): Boolean {
        return t1?.equals(t2, ignoreCase = true) ?: false

    }

    /**
     *
     * @Title: convertToList
     *
     * @Description: Convert datas to a list.
     *
     * @param @param
     * ts @param @return @return List<T> @throws
    </T> */
    fun <T> convertToList(vararg ts: T): List<T>? {
        val tList = ArrayList<T>()

        for (t in ts) {
            tList.add(t)
        }

        return if (tList.size > 0) tList else null
    }

    /**
     *
     * @Title: addList
     *
     * @Description: Merge two list to a new list.
     *
     * @param @param
     * list1 @param @param list2 @param @return @return List
     * <T> @throws
    </T> */
    fun <T> addList(list1: MutableList<T>, list2: MutableList<T>): List<T> {

        val newList = ArrayList<T>()

        if (!isEmpty(list1)) {
            newList.addAll(list1)
        }

        if (!isEmpty(list2)) {
            newList.addAll(list2)
        }

        return newList
    }

    /**
     *
     * @Title: arrayToList
     *
     * @Description: Convert a array to a new list.
     *
     * @param @param
     * t @param @return @return List<T> @throws
    </T> */
    fun <T> arrayToList(t: Array<T>): List<T> {
        return Arrays.asList(*t)
    }

    fun arrayToList(arrayID: Int): List<String> {
        return arrayToList(ctx.resources.getStringArray(arrayID))
    }

    fun <T> isEmpty(t: Array<T>?): Boolean {
        return t?.isEmpty() ?: true
    }

    fun <T> isEmpty(list: List<T>?): Boolean {
        return list?.isEmpty() ?: true
    }

    fun <T> isEmpty(list: Set<T>?): Boolean {
        return list?.isEmpty() ?: true
    }

    fun <K, V> isEmpty(map: Map<K, V>?): Boolean {
        return map?.isEmpty() ?: true
    }

    /**
     * 按指定大小，分隔集合，将集合按规定个数分为n个部分
     *
     * @param list
     * @param len
     * @return
     */
    fun <T> splitList(list: List<T>?, len: Int): List<List<T>>? {
        if (isEmpty(list)) {
            return null
        }

        val result = ArrayList<List<T>>()


        val size = list!!.size
        val count = (size + len - 1) / len


        for (i in 0 until count) {
            val subList = list.subList(i * len, if ((i + 1) * len > size) size else len * (i + 1))
            result.add(subList)
        }
        return result
    }
}