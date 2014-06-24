/**
 * Created by any on 2014/6/24.
 */

var crypto = require('crypto');
var http = require('http');

var key = "70URxtirPN2sFtiEAu2nlwVS59RMUZ";
var accessKeyId = "eKJUbg7d49ojjYCf";

var subResource = "cors";

function ossRequest(verb, contentType, bucketName, objectName, content) {
    var shasum = crypto.createHash('sha1');
    var contentMD5 = shasum.update(content, 'utf8');
    contentMD5 = contentMD5.digest('base64');
    var opDate = new Date().toGMTString();
    var CanonicalizedOSSHeaders = "";
    var CanonicalizedResource = "";
    CanonicalizedResource = "/" + bucketName + "/" + objectName + "?" + subResource;


    console.log(opDate);

    var text = verb + "\n"
        + "" + "\n"
        + contentType + "\n"
        + opDate + "\n"
        + CanonicalizedOSSHeaders
        + CanonicalizedResource;

    console.log(text);

    var signature = crypto.createHmac('sha1', key).update(text).digest('base64');

    console.log(signature);

    //The url we want is: 'www.random.org/integers/?num=1&min=1&max=10&col=1&base=10&format=plain&rnd=new'
    var options = {
        host: 'panor-static.oss-cn-qingdao.aliyuncs.com',
        path: '/?' + subResource,
        method: verb,
        headers: {
            Authorization: "OSS " + accessKeyId + ":" + signature,
            Date: opDate,
//            'Content-Md5': contentMD5,
            'Content-Type': contentType
        }
    };

    var callback = function(response) {
        var str = '';

        //another chunk of data has been recieved, so append it to `str`
        response.on('data', function (chunk) {
            str += chunk;
        });

        //the whole response has been recieved, so we just print it out here
        response.on('end', function () {
            console.log(str);
        });
    };

    var req = http.request(options, callback);
    req.write(content);
    req.end();

    return signature;
}

//ossRequest("PUT", "application/xml", "panor-static", "", '<?xml version="1.0" encoding="UTF-8"?><CORSConfiguration><CORSRule><AllowedOrigin>*</AllowedOrigin><AllowedMethod>PUT</AllowedMethod><AllowedMethod>GET</AllowedMethod><AllowedHeader>*</AllowedHeader><MaxAgeSeconds>1000000</MaxAgeSeconds></CORSRule></CORSConfiguration>');

//ossRequest("GET", "", "panor-static", "", '');

function ossOptions(verb) {
    //The url we want is: 'www.random.org/integers/?num=1&min=1&max=10&col=1&base=10&format=plain&rnd=new'
    var options = {
        host: 'panor-static.oss-cn-qingdao.aliyuncs.com',
        path: '/',
        method: verb,
        headers: {
            Origin: "http://www.photoshows.cn",
            'Access-Control-Request-Method': "GET",
            'Access-Control-Request-Headers': "origin"
        }
    };

    var callback = function(response) {

//        console.log(JSON.stringify(response.headers));
        for(var item in response.headers) {
            console.log(item + ": " + response.headers[item]);
        }

        var str = '';

        //another chunk of data has been recieved, so append it to `str`
        response.on('data', function (chunk) {
            str += chunk;
        });

        //the whole response has been recieved, so we just print it out here
        response.on('end', function () {
            console.log(str);
        });
    };

    var req = http.request(options, callback);
    req.end();
}

//ossOptions("OPTIONS");

function panorStatic(verb) {
    //The url we want is: 'www.random.org/integers/?num=1&min=1&max=10&col=1&base=10&format=plain&rnd=new'
    var options = {
        host: 'static.photoshows.cn',
        path: '/2.jpg@!photo-360',
        method: verb,
        headers: {
            Origin: "http://www.photoshows.cn"
//            'Access-Control-Request-Method': "GET",
//            'Access-Control-Request-Headers': "origin"
        }
    };

    var callback = function(response) {

//        console.log(JSON.stringify(response.headers));
        for(var item in response.headers) {
            console.log(item + ": " + response.headers[item]);
        }

        var str = '';

        //another chunk of data has been recieved, so append it to `str`
//        response.on('data', function (chunk) {
//            str += chunk;
//        });

        //the whole response has been recieved, so we just print it out here
        response.on('end', function () {
//            console.log(str);
        });
    };

    var req = http.request(options, callback);
    req.end();
}

panorStatic("GET");