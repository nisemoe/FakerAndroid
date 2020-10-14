package com.facker.toolchain.api.xbase;

import com.facker.toolchain.utils.Logger;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManifestEditor {
    public static final String TAG_ACTIVITY = "activity";
    public static final String TAG_SERVICE = "service";
    public static final String TAG_PROVIDER = "provider";
    public static final String TAG_INTENT_FILTER = "intent-filter";

    /**
     * manifest 路径
     */
    File manifestFile;

    /**
     *
     */
    Document document;


    /**
     * manifest
     */
    Element manifestElement;

    /**
     * application 节点
     */
    Element applicationElement;

    public ManifestEditor(File manifestFile) throws DocumentException {
        this.manifestFile = manifestFile;
        SAXReader reader = new SAXReader();
        document = reader.read(manifestFile);
        manifestElement = document.getRootElement();
        applicationElement = manifestElement.element("application");
    }

    public Element getManifestElement(){
        return manifestElement;
    }
    /**
     * @return 原包名
     */
    public String getPackagenName() {
        return manifestElement.attributeValue("package");
    }

    public String getVersionName() {
        return manifestElement.attributeValue("package");
    }


    public String getNetConfig(){
        return applicationElement.attributeValue("networkSecurityConfig");
    }
    /**
     *
     * @param elementFrom
     * @param elementTo
     */
    public void copyApplicaionElements(Element elementFrom,Element elementTo) {//TODO 如果是入口Activity的话则跳过
        List<Element> elements = elementFrom.elements();
        for (Element element : elements) {
            //排除测试代码
            if(isMainActivityElement(element)){
                continue;
            }
            Element copy = element.createCopy();
            elementTo.add(copy);
        }
    }




    public Element getApplicationElement(){
        return applicationElement;
    }
    /**
     * 原游戏名称
     *
     * @return
     */
    public String getAppName() {
        return applicationElement.attributeValue("label");
    }

    /**
     * @return
     */
    public String getApplicationName() {
        return applicationElement.attributeValue("name");
    }

    public String getApplicationDebuggable() {
        return applicationElement.attributeValue("debuggable");
    }

    public List<String> getUsesPermissions(){
        List<String> permissions = new ArrayList<>();
        List<Element> permissionElements = manifestElement.elements("uses-permission");
        for (Element element : permissionElements) {
            permissions.add(element.attributeValue("name"));
        }
        return permissions;
    }

    public List<String> getPermissions(){
        List<String> permissions = new ArrayList<>();
        List<Element> permissionElements = manifestElement.elements("permission");
        for (Element element : permissionElements) {
            permissions.add(element.attributeValue("name"));
        }
        return permissions;
    }

    public String getLogoFileName(){
        String logoValue =  applicationElement.attributeValue("icon");
        if(logoValue.contains("/")){
            String logoFileName = logoValue.split("/")[1];
            return logoFileName;
        }
        return null;
    }

    public String getNetworkSecurityConfig() {
        String networkSecurityConfig = applicationElement.attributeValue("networkSecurityConfig");
        if(networkSecurityConfig==null){
            return null;
        }
        return networkSecurityConfig.replace("@xml/","");
    }

    public void modNetworkSecurityConfig(String name) {
        Attribute isSelfAttr = applicationElement.attribute("networkSecurityConfig");
        if(isSelfAttr!=null){
            isSelfAttr.setValue(String.format("@xml/%s", name));
        }else{
            applicationElement.addAttribute("android:networkSecurityConfig", String.format("@xml/%s", name));
        }
    }

    /**
     * 获取启动act
     *
     * @return
     */
    public Element getLancherActivityElement() {
        /**
         * 列出所有Activity
         */
        List<Element> activityElements = applicationElement.elements(TAG_ACTIVITY);
        for (Element activityElement : activityElements) {
            if (isMainActivityElement(activityElement)) return activityElement;
        }
        return null;
    }



    private  boolean isProvider(Element element){
        if("provider".equals(element.getName())){
                return true;
        }
        return false;
    }
    private boolean isMainActivityElement(Element activityElement) {
        List<Element> filterElements = activityElement.elements("intent-filter");
        for (Element filterElement : filterElements) {
            List<Element> actionElements = filterElement.elements("action");
            boolean main =false;
            for (Element actionElement : actionElements) {
                if ("android.intent.action.MAIN".equals(actionElement.attributeValue("name"))) {
                    main = true;
                    break;
                }
            }

            boolean launcher = false;
            List<Element> categoryElements = filterElement.elements("category");
            for (Element categoryElement : categoryElements) {
                if ("android.intent.category.LAUNCHER".equals(categoryElement.attributeValue("name"))) {
                    launcher = true;
                    break;
                }
            }

            if(main&&launcher){
                return true;
            }
        }
        return false;
    }

    public List<String> getActivityNames() {

        List<String> strings = new ArrayList<>();
        List<Element> activityElements = applicationElement.elements(TAG_ACTIVITY);
        for (Element activityElement : activityElements) {
            String name = activityElement.attributeValue("name");
            strings.add(name);
        }
        return strings;
    }


    public String getLancherActivityName() {
        Element activityElement = getLancherActivityElement();

        if(activityElement==null){
            return "";
        }
        //该ACT为入口类
        String name = activityElement.attributeValue("name");
        if (name.startsWith(".")) {
            return getPackagenName() + name;
        }
        return name;
    }

    public void modModelProviderAuthorities(String domain,String replaceValue){
            List<Element> providerElements = getProviderElements();
            for (Element element:providerElements) {
                if(isModelProvider(element,domain)){
                    String authorities = element.attributeValue("authorities");
                    authorities = authorities.replace(domain,replaceValue);
                    Attribute isSelfAttr = element.attribute("authorities");
                    isSelfAttr.setValue(authorities);
                }
        }

    }
    public List<Element> getProviderElements() {
        return applicationElement.elements(TAG_PROVIDER);
    }

    boolean isModelProvider(Element element,String domain){

        if(element.attributeValue("authorities").contains(domain)){
            return true;
        }
        return false;
    }

    /**
     * 增加meta
     *
     * @param metaKey
     * @param metaValue
     */
    public void insertMeta(String metaKey, String metaValue) {
        if (checkIsExist(applicationElement, "meta-data", metaKey)) {
            throw new RuntimeException();
        }
        Element metaDataElement = applicationElement.addElement("meta-data");
        metaDataElement.addAttribute("android:name", metaKey);
        metaDataElement.addAttribute("android:value", metaValue);
    }

    public void coverMeta(String metaKey, String metaValue) {
        if (checkIsExist(applicationElement, "meta-data", metaKey)) {
            modMeta(metaKey,metaValue);
            return;
        }
        Element metaDataElement = applicationElement.addElement("meta-data");
        metaDataElement.addAttribute("android:name", metaKey);
        metaDataElement.addAttribute("android:value", metaValue);
    }

    public void modMeta(String name,String value) {
        List<Element> metaDataElements = applicationElement.elements("meta-data");
        for (Element element: metaDataElements) {
            String metaName = element.attributeValue("name");
            System.out.println("metaName------"+metaName);
            if(metaName.equals(name)){
                element.attribute("value").setValue(value);
            }
        }
    }

    /**
     * 增加权限
     *
     * @param name
     */
    public void insertUsesPermission(String name) {
        if (checkIsExist(manifestElement, "uses-permission", name)) {
            return;
        }
        Element metaDataElement = manifestElement.addElement("uses-permission");
        metaDataElement.addAttribute("android:name", name);
    }

    public void insertPermission(String name,String protectLavel) {
        if (checkIsExist(manifestElement, "permission", name)) {
            return;
        }
        Element metaDataElement = manifestElement.addElement("permission");
        metaDataElement.addAttribute("android:name", name);
        if(!TextUtil.isEmpty(protectLavel)){
            metaDataElement.addAttribute("android:protectionLevel", protectLavel);
        }
    }
    /**
     * 普通的act
     */
    public void insertActivity(String name, String screenOrientation, String theme, String configChanges, String process) {

        if (checkIsExist(applicationElement, TAG_ACTIVITY, name)) {
            throw new RuntimeException();
        }
        Element activityElement = applicationElement.addElement(TAG_ACTIVITY);
        setActivityAttribute(name, screenOrientation, theme, configChanges, process, activityElement);
    }

    public void insertMainActivity(String name, String screenOrientation, String theme, String configChanges, String process) {

        if (checkIsExist(applicationElement, TAG_ACTIVITY, name)) {
            throw new RuntimeException();
        }
        Element activityElement = applicationElement.addElement(TAG_ACTIVITY);
        setActivityAttribute(name, screenOrientation, theme, configChanges, process, activityElement);
        Element intentFilter = activityElement.addElement("intent-filter");
        Element action = intentFilter.addElement("action");
        action.addAttribute("android:name", "android.intent.action.MAIN");
        Element category1 = intentFilter.addElement("category");
        category1.addAttribute("android:name", "android.intent.category.LAUNCHER");
//        Element category2 = intentFilter.addElement("category");
//        category2.addAttribute("android:name","android.intent.category.LEANBACK_LAUNCHER");

    }

    /**
     * vivo
     * 定制版
     *
     * @param name
     * @param screenOrientation
     * @param theme
     * @param configChanges
     * @param proress
     */
    public void insertActivityVivoWithFilter(String name, String screenOrientation, String theme, String configChanges, String proress) {
        if (checkIsExist(applicationElement, TAG_ACTIVITY, name)) {
            return;
        }
        Element activityElement = applicationElement.addElement(TAG_ACTIVITY);
        setActivityAttribute(name, screenOrientation, theme, configChanges, proress, activityElement);
        Element filter = activityElement.addElement("intent-filter");
        Element action = filter.addElement("action");
        action.addAttribute("android:name", "android.intent.action.VIEW");
        Element category1 = filter.addElement("category");
        category1.addAttribute("android:name", "android.intent.category.DEFAULT");
        Element category2 = filter.addElement("category");
        category2.addAttribute("android:name", "android.intent.category.BROWSABLE");
        Element data = filter.addElement("data");
        data.addAttribute("android:host", "union.vivo.com");
        data.addAttribute("android:path", "/openjump");
        data.addAttribute("android:scheme", "vivounion");
    }

    /**
     * 插入服务
     *
     * @param name
     */
    public void insertService(String name, String process, String priority, String exported) {
        if (checkIsExist(applicationElement, TAG_SERVICE, name)) {
            return;
        }
        Element serviceElement = applicationElement.addElement(TAG_SERVICE);
        serviceElement.addAttribute("android:name", name);
        if (!TextUtil.isEmpty(process)) {
            serviceElement.addAttribute("android:process", process);
        }
        if (!TextUtil.isEmpty(priority)) {
            serviceElement.addAttribute("android:priority", priority);
        }
        if (!TextUtil.isEmpty(exported)) {
            serviceElement.addAttribute("android:exported", exported);
        }
    }

    /**
     * 插入provider
     * @param name
     * @param authorities
     * @param exported
     * @param grantUriPermissions
     * @param metaName
     * @param metaResource
     */
    public void insertProvider(String name, String authorities, String exported, String grantUriPermissions, String metaName, String metaResource) {
        if (checkIsExist(applicationElement, TAG_PROVIDER, name)) {
            return;
        }
        Element provicerElement = applicationElement.addElement(TAG_PROVIDER);
        provicerElement.addAttribute("android:name", name);
        if (!TextUtil.isEmpty(authorities)) {
            provicerElement.addAttribute("android:authorities", authorities);
        }
        if (!TextUtil.isEmpty(exported)) {
            provicerElement.addAttribute("android:exported", exported);
        }
        if (!TextUtil.isEmpty(grantUriPermissions)) {
            provicerElement.addAttribute("android:grantUriPermissions", grantUriPermissions);
        }
        if (!TextUtil.isEmpty(metaName)) {
            Element meta = provicerElement.addElement("meta-data");
            meta.addAttribute("android:name", metaName);
            meta.addAttribute("android:resource", metaResource);
        }
    }

    private boolean checkIsExist(Element element, String tag, String name) {
        List<Element> originalElements = element.elements(tag);
        for (Element originalElement : originalElements) {
            String originalElementName = originalElement.attributeValue("name");
            if (name.equals(originalElementName)) {
                Logger.log("Tag :" + element.getName() + "名称：" + name + "已经存在");
                return true;
            }
        }
        return false;
    }

    public String getMateValue(String name) {
        List<Element> originalElements = applicationElement.elements("meta-data");
        for (Element originalElement : originalElements) {
            String originalElementName = originalElement.attributeValue("name");
            if (name.equals(originalElementName)) {
                Logger.log("Tag :" + applicationElement.getName() + "名称：" + name + "已经存在");
                return originalElement.attributeValue("value");
            }
        }
        return null;
    }

    public List<String> getMateNames() {
        List<Element> originalElements = applicationElement.elements("meta-data");
        List<String> metaNames = new ArrayList<>();
        for (Element originalElement : originalElements) {
            String originalElementName = originalElement.attributeValue("name");
            metaNames.add(originalElementName);
        }
        return metaNames;
    }


    private void setActivityAttribute(String name, String screenOrientation, String theme, String configChanges, String proress, Element activityElement) {
        activityElement.addAttribute("android:name", name);
        if (!TextUtil.isEmpty(screenOrientation)) {
            activityElement.addAttribute("android:screenOrientation", screenOrientation);
        }
        if (!TextUtil.isEmpty(theme)) {
            activityElement.addAttribute("android:theme", theme);
        }
        if (!TextUtil.isEmpty(configChanges)) {
            activityElement.addAttribute("android:configChanges", configChanges);
        }
        if (!TextUtil.isEmpty(proress)) {
            activityElement.addAttribute("android:process", proress);
        }
    }

    /**
     * 插入权限
     *
     * @param name
     */
    public void modApplication(String name) {
        String applicationName = getApplicationName();
        if (null == applicationName) {
            applicationElement.addAttribute("android:name", name);
        } else {
            Attribute isSelfAttr = applicationElement.attribute("name");
            isSelfAttr.setValue(name);
        }

        String applicationDebuggable = getApplicationDebuggable();
        if (null == applicationDebuggable) {
            applicationElement.addAttribute("android:debuggable", "true");
        } else {
            Attribute isSelfAttr = applicationElement.attribute("debuggable");
            isSelfAttr.setValue("true");
        }
    }

    public void modApplicationTheme(String theme) {
        Attribute isSelfAttr = applicationElement.attribute("theme");
        if (isSelfAttr == null) {
            applicationElement.addAttribute("android:theme", theme);
        } else {
            isSelfAttr.setValue(theme);
        }


    }

    public void modPkg(String name) {
        Attribute isSelfAttr = manifestElement.attribute("package");
        isSelfAttr.setValue(name);
    }

    /**
     * 修改游戏名称
     *
     * @param name
     */
    public void modApplicationLabel(String name) {
        Attribute isSelfAttr = applicationElement.attribute("label");
        isSelfAttr.setValue(name);
    }

    public void modLancherActivityLabel(String name) {
        Element lancherActivityElement = getLancherActivityElement();
        if(lancherActivityElement==null){
            return;
        }
        Attribute isSelfAttr = lancherActivityElement.attribute("label");
        if (isSelfAttr == null) {
            lancherActivityElement.addAttribute("android:label", name);
        } else {
            isSelfAttr.setValue(name);
        }

    }

    public void modLancherActivityScreenOrientation(String screenOrientation) {
        Element lancherActivityElement = getLancherActivityElement();
        Attribute isSelfAttr = lancherActivityElement.attribute("screenOrientation");
        isSelfAttr.setValue(screenOrientation);
    }

    public void modLancherActivityTheme(String theme) {
        Element lancherActivityElement = getLancherActivityElement();
        Attribute isSelfAttr = lancherActivityElement.attribute("theme");
        if (isSelfAttr == null) {
            lancherActivityElement.addAttribute("android:theme", theme);
        } else {
            isSelfAttr.setValue(theme);
        }
    }

    public void modLancherActivityIntent() {
        Element lancherActivityElement = getLancherActivityElement();
        List<Element> intentFilters = lancherActivityElement.elements("intent-filter");
        for (Element ele : intentFilters) {
            lancherActivityElement.remove(ele);
        }
    }


    public static void main(String[] args) {
        try {
            ManifestEditor manifestMod = new ManifestEditor(new File("AndroidManifest.xml"));
            String appName = manifestMod.getAppName();
            String packageName = manifestMod.getPackagenName();
            String applicationName = manifestMod.getApplicationName();
            Logger.log("app name -- " + appName + "| package name -- " + packageName + "| application name -- " + applicationName);
            String lancherActivity = manifestMod.getLancherActivityName();
            Logger.log("启动入口类为：" + lancherActivity);
            manifestMod.insertMeta("mybatis/mybatis-config.xml", "100");
            manifestMod.insertMeta("y", "200");
            manifestMod.insertUsesPermission("fuck");
            manifestMod.modApplication("comn.xdada.mybatis-config.xml");
            manifestMod.modLancherActivityLabel("这是的战争");
            manifestMod.modApplicationLabel("这是我的战争2");
            manifestMod.insertActivity("xxx", "landscape", "@android:style/Theme.NoTitleBar.Fullscreen", "keyboardHidden|orientation|screenSize", ":gsdk");
            manifestMod.insertActivityVivoWithFilter("com.vivo.unionsdk.ui.UnionActivity", "landscape", "@android:style/Theme.NoTitleBar.Fullscreen", "keyboardHidden|orientation|screenSize", ":gsdk");
            manifestMod.insertService("com.qq.e.comm.DownloadService", null, null, "false");
            manifestMod.insertProvider("android.support.v4.content.FileProvider", "com.qq.e.union.demo.fileprovider", "false", "true", null, null);
            manifestMod.save();
        } catch (Exception e) {
            Logger.log("manifest 修改失败");
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
        xmlFormat.setEncoding("UTF-8");
        // 设置换行
        xmlFormat.setNewlines(true);
        // 生成缩进
        xmlFormat.setIndent(true);
        // 使用4个空格进行缩进, 可以兼容文本编辑器
        xmlFormat.setIndent("    ");

        XMLWriter writer = new XMLWriter(new FileWriter(manifestFile), xmlFormat);
        writer.write(document);
        writer.close();
    }
}

