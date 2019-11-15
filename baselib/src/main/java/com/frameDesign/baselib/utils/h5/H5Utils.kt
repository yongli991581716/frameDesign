package com.frameDesign.baselib.utils.h5

object H5Utils {
    /**
     * 拼装get url
     * @param url String
     * @param parameterSelector HashMap<String, String>.() -> Unit
     * @return String
     */
    fun encQueryUrl(url: String, parameterSelector: HashMap<String, String>.() -> Unit): String {
        val map = HashMap<String, String>()
                .apply(parameterSelector)

        return encQueryUrl(url, map)
    }

    /**
     * 拼装get url
     * @param url String
     * @param parameterSelector HashMap<String, String>.() -> Unit
     * @return String
     */
    fun encQueryUrl(url: String, queryMap: HashMap<String, String>): String {
        val entries = queryMap.entries

        if (entries.isNotEmpty()) {
            val sb = StringBuffer(url)

            var hasMask = false

            val ite = entries.iterator()

            while (ite.hasNext()) {
                val (k, v) = ite.next()

                if (k.isNotEmpty() && v.isNotEmpty()) {
                    if (hasMask) {
                        sb.append('&')
                    } else {
                        hasMask = true
                        sb.append('?')
                    }
                            .append(k)
                            .append('=')
                            .append(v)
                }
            }

            return sb.toString()
        }

        return url
    }
}