//
// Created by Fate on 2020-06-01.
//

#ifndef U3DINJECT_MASTER_MONOSTRING_H
#define U3DINJECT_MASTER_MONOSTRING_H

#include <string>
#include <stdint.h>

#ifdef __GNUC__

#include <endian.h>

#endif // __GNUC__
using namespace std;

class MonoString {
    void *klass;
    void *monitor;
    int length;
    char chars[1];
    char *getChars() {
        return chars;
    }

public:
    /**
     * 获取字符串长度
     * @return
     */
    int getLength() {
        return length;
    }
    /**
        * monostring转char*
        * @return
        */
    const char *toChars();

    /**
     * monostring转string
     * @return
     */
    string toString();

    /**
 * char* 转monostring
 * @param s
 */
    void setMonoString(const char *s);

    /**
     * string 转monostring
     * @param s
     */
    void setMonoString(string s);
};

// 从UTF16编码字符串构建，需要带BOM标记
std::string utf16_to_utf8(const std::u16string &u16str);

// 从UTF16 LE编码的字符串创建
std::string utf16le_to_utf8(const std::u16string &u16str);

// 从UTF16BE编码字符串创建
std::string utf16be_to_utf8(const std::u16string &u16str);

// 获取转换为UTF-16 LE编码的字符串
std::u16string utf8_to_utf16le(const std::string &u8str, bool addbom = false, bool *ok = NULL);

// 获取转换为UTF-16 BE的字符串
std::u16string utf8_to_utf16be(const std::string &u8str, bool addbom = false, bool *ok = NULL);

#endif //U3DINJECT_MASTER_MONOSTRING_H

