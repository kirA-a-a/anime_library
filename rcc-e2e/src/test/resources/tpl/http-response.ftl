<#ftl output_format="HTML">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link type="text/css" href="https://yandex.st/highlightjs/8.0/styles/github.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/highlight.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/json.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/xml.min.js"></script>
    <script type="text/javascript">
        document.addEventListener('DOMContentLoaded', function () {
            var blocks = document.querySelectorAll('pre code');
            for (var i = 0; i < blocks.length; i++) {
                hljs.highlightBlock(blocks[i]);
            }
        });
    </script>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
            font-size: 14px;
            color: #24292e;
            margin: 12px;
        }
        .line {
            margin-bottom: 8px;
            font-weight: 600;
        }
        .status {
            color: #22863a;
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
    </style>
</head>
<body>
<div class="line">
    Status code <span class="status"><#if data.responseCode??>${data.responseCode}<#else>Unknown</#if></span>
</div>
<#if data.url??><div class="line">${data.url}</div></#if>

<#if data.body??>
    <h4>Body</h4>
    <pre><code><#t>${data.body}</code></pre>
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
</body>
</html>
