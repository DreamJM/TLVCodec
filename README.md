# TLVCodec
TLV Message Codec Library

## TLV Format Introduction
### Default Header
Default Header consists of 2 bytes Message Type, 2 bytes Message Length and 4 bytes Message Identity. 

|&nbsp;Message Type&nbsp;&nbsp;&nbsp;(2 bytes)&nbsp;&nbsp;|<br/>
|&nbsp;Message Length(2 bytes)&nbsp;&nbsp;|<br/>
|&nbsp;Message Identity(4 bytes)&nbsp;|<br/>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;... TLV Fields ... &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|<br/>

Message Length is the total byte size of TLV Message body and Message Identity.<br/>

If you want to customize TLV Message Header, you can implements [IHeaderCodec](src/main/java/com/dream/codec/tlv/header/IHeaderCodec.java) 
and pass it as constructor parameter when using [RawTLVCodec](src/main/java/com/dream/codec/tlv/codec/RawTLVCodec.java).

### TLV Field

***Default TLV Field***<br/>
|&nbsp;Tag&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(2 bytes)&nbsp;|<br/>
|&nbsp;Length&nbsp;&nbsp;&nbsp;(1 byte)&nbsp;|<br/>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...Value...&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|<br/>

***Long Type TLV Field***<br/>
|&nbsp;Tag&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(2 bytes)&nbsp;|<br/>
|&nbsp;Length&nbsp;&nbsp;&nbsp;(2 bytes)&nbsp;|<br/>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...Value...&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|<br/>

Length is the total byte size of ***Value***.
To use Long Type TLV Field, you can define field as following:
```
@TLVField(tag = 0x09, fieldLenType = FieldLenType.LONG)
private String str;
```
### Wrapped TLV Field
***Value*** can also consist of TLV Fields.<br/>
You can use ***@TLVObject*** to realize it, see [DemoObj](src/test/java/com/dream/codec/tlv/bean/DemoObj.java) for example. 

### List Type Field
List Type Field is a special Wrapping TLV Field with all child fields are the same kind.<br/>
You can use Multiple TLVField Annotation to define it.
```
@TLVField(tag = 0x28)
@TLVField(tag = 0x29)
private List<DemoObj> listObjs;
```

## Usage
### Installation
##### Using Jar Directory
[Packaged Jar](version/tlv-1.0.jar) is under ***'version'*** directory.

##### Installing Locally
```
mvn install
```
After installing, you can add dependency in your pom.xml
```
<dependency>
    <groupId>com.dream.codec</groupId>
    <artifactId>tlv</artifactId>
    <version>1.0</version>
</dependency>
```
Or Gradle
```
repositories {
    mavenLocal()
    ...
}

dependencies {
    ...
    compile('com.dream.codec:tlv:1.0')
    ...
}
```

### Code Example
[Demo Detail](src/test/java/com/dream/codec/tlv/DemoTest.java)
##### TLV Message Definition
```
@TLVMsgBean(type = 2)
public class BaseMsg extends TLVMsg {

    @TLVField(tag = 0xD0, index = 999, require = true, charset = TLVCharset.ASCII)
    private String baseString;

    @TLVField(tag = 0xD1, index = -1, require = true)
    private int baseInt;
    
    // Non-Parameter Constructor is Required
    public BaseMsg() {

    }

    ...
}
```
##### Codec
```
// Init context to scan specific package for TLV Message
TLVContext context = new TLVContext().init("com.dream.codec.tlv.bean");
TLVCodec codec = new TLVCodec(context);

// Encode Object to TLV Bytes
DemoMsg msg = new DemoMsg("hello", 1, 123456);
byte[] encodeResult = codec.encode(msg);

// Decode TLV Bytes to Object
DemoMsg decodedMsg = codec.convertTo(codec.decode(encodeResult), DemoMsg.class);
assertEquals(decodedMsg, msg);
```

## License
TLVCodec is released under the [Apache 2.0 license](LICENSE)

    Copyright 2018 Meng Jiang.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
