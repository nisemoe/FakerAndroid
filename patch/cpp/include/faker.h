
void onJniLoad(JavaVM *vm, void *reserved);
/**
 * 安装修改后合并的DEX
 * @param env
 * @param instance
 * @param base
 * @param fakeDexAssetFileName  修改并合并后的DEX在Asset跟目录的文件名称
 */
void fakeDex(JNIEnv *env, jobject instance,jobject base,char *fakeDexAssetFileName);

/**
 * 替换原有的application
 * @param env
 * @param instance
 * @param application
 * @param targetApp  原应用的Application名称
 * @return
 */
void fakeApp(JNIEnv *env, jobject instance,jobject application,jboolean b);

/**
 * 获取So文件 基础地址
 * @param soname 例:libil2cpp.so
 * @return
 */
long baseAddr(char *soname);


/**
 * Hook so 函数 = baseAddr+offset 函数偏移地址
 * @param function_address 原函数地址
 * @param replace_call 新函数
 * @param origin_call  虚构原函数，用于对原函数进行编码调用
 */
void fakeCpp(void *function_address, void *replace_call,void **origin_call);


/**
 * 安装一个普通的DEX(非合并)
 * @param path
 * @return
 */
bool installDex(JNIEnv* env, jobject jobject, string dexPath);


/**
 * 安装多个普通DEX(非合并)
 * @param path
 * @return
 */
bool installDex(JNIEnv* env, jobject jobject, vector<string>* dexPaths);
/**
 *
 * 1、获取so基础地址
 *
 * 2、hook so 函数
 *
 * 3、const char* coverIl2cppString2Char(Il2CppString *str);
 *
 * 4、dexInstaller 安装非合并DEX
 *
 * 5、fakeCpp
 */


