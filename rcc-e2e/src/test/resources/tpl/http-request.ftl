<#ftl output_format="HTML">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpRequestAttachment" -->
<#assign curlPretty = data.curl!""
  ?replace(" -X ", " \\\n  -X ")
  ?replace(" -H ", " \\\n  -H ")
  ?replace(" -d ", " \\\n  -d ")
  ?replace(" --data ", " \\\n  --data ")
>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link type="text/css" href="https://yandex.st/highlightjs/8.0/styles/github.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/highlight.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/json.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/xml.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/bash.min.js"></script>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
            font-size: 14px;
            color: #24292e;
            margin: 12px;
        }
        .line {
            margin-bottom: 12px;
            font-weight: 600;
        }
        .method {
            color: #005cc5;
            margin-right: 6px;
        }
        .url {
            color: #24292e;
            word-break: break-all;
        }
        h4 {
            margin: 16px 0 8px;
            font-size: 13px;
            text-transform: uppercase;
            letter-spacing: 0.04em;
            color: #586069;
        }
        table.kv {
            border-collapse: collapse;
            width: 100%;
            font-family: ui-monospace, SFMono-Regular, Menlo, Consolas, monospace;
            font-size: 12px;
        }
        table.kv td {
            padding: 4px 8px;
            border-bottom: 1px solid #eaecef;
            vertical-align: top;
        }
        table.kv td.key {
            width: 220px;
            color: #d73a49;
            white-space: nowrap;
        }
        table.kv td.val {
            color: #032f62;
            word-break: break-all;
        }
        pre {
            white-space: pre-wrap;
            word-break: break-word;
            margin: 0;
            border-radius: 6px;
            background: #f6f8fa;
            padding: 12px;
        }
        .curl-wrap {
            position: relative;
        }
        .curl-wrap pre {
            padding-top: 36px;
        }
        .copy-btn {
            position: absolute;
            top: 8px;
            right: 8px;
            z-index: 1;
            cursor: pointer;
            border: 1px solid #d1d5da;
            background: #fff;
            border-radius: 6px;
            padding: 4px 10px;
            font-size: 12px;
            color: #24292e;
        }
        .copy-btn:hover {
            background: #f3f4f6;
        }
        .copy-btn.ok {
            border-color: #28a745;
            color: #28a745;
        }
    </style>
    <script type="text/javascript">
        function copyCurl() {
            var el = document.getElementById('curl-cmd');
            var btn = document.getElementById('copy-curl-btn');
            var text = el.getAttribute('data-raw') || el.innerText || el.textContent;
            var done = function () {
                btn.textContent = 'Copied!';
                btn.classList.add('ok');
                setTimeout(function () {
                    btn.textContent = 'Copy curl';
                    btn.classList.remove('ok');
                }, 1500);
            };
            if (navigator.clipboard && navigator.clipboard.writeText) {
                navigator.clipboard.writeText(text).then(done).catch(function () {
                    fallbackCopy(text, done);
                });
            } else {
                fallbackCopy(text, done);
            }
        }
        function fallbackCopy(text, done) {
            var ta = document.createElement('textarea');
            ta.value = text;
            document.body.appendChild(ta);
            ta.select();
            try { document.execCommand('copy'); } catch (e) {}
            document.body.removeChild(ta);
            done();
        }
        document.addEventListener('DOMContentLoaded', function () {
            var blocks = document.querySelectorAll('pre code');
            for (var i = 0; i < blocks.length; i++) {
                hljs.highlightBlock(blocks[i]);
            }
        });
    </script>
</head>
<body>
<div class="line">
    <span class="method"><#if data.method??>${data.method}<#else>GET</#if></span>
    <span class="url"><#if data.url??>${data.url}<#else>Unknown</#if></span>
</div>

<#if data.body??>
    <h4>Body</h4>
    <#if data.body?contains("=") && !(data.body?trim?starts_with("{") || data.body?trim?starts_with("[") || data.body?trim?starts_with("<"))>
        <table class="kv">
            <#list data.body?split("&") as pair>
                <#if pair?contains("=")>
                    <tr>
                        <td class="key">${pair?keep_before("=")}</td>
                        <td class="val">${pair?keep_after("=")}</td>
                    </tr>
                <#else>
                    <tr>
                        <td class="key" colspan="2">${pair}</td>
                    </tr>
                </#if>
            </#list>
        </table>
    <#else>
        <pre><code><#t>${data.body}</code></pre>
    </#if>
</#if>

<#if (data.headers)?has_content>
    <h4>Headers</h4>
    <table class="kv">
        <#list data.headers as name, value>
            <tr>
                <td class="key">${name}</td>
                <td class="val">${value!"null"}</td>
            </tr>
        </#list>
    </table>
</#if>

<#if (data.cookies)?has_content>
    <h4>Cookies</h4>
    <table class="kv">
        <#list data.cookies as name, value>
            <tr>
                <td class="key">${name}</td>
                <td class="val">${value!"null"}</td>
            </tr>
        </#list>
    </table>
</#if>

<#if (data.formParams)?has_content>
    <h4>Form params</h4>
    <table class="kv">
        <#list data.formParams as name, value>
            <tr>
                <td class="key">${name}</td>
                <td class="val">${value!"null"}</td>
            </tr>
        </#list>
    </table>
</#if>

<#if data.curl??>
    <h4>Curl</h4>
    <div class="curl-wrap">
        <button id="copy-curl-btn" class="copy-btn" type="button" onclick="copyCurl()">Copy curl</button>
        <pre><code id="curl-cmd" class="bash" data-raw="${data.curl}">${curlPretty}</code></pre>
    </div>
</#if>
</body>
</html>
