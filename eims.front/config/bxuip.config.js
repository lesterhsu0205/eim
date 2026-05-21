/**
 * Created by UI/UX Team on 2018. 1. 19..
 */

const path = require('path');
const contextPath = path.join(__dirname, '..');
const srcPath = path.join(contextPath, 'src');

module.exports = {
    context: contextPath,
    environment: 'env.dev.js',
    copy: [
        {
            context: srcPath,
            from: 'app/**/*',
            ignore: ['*.js']
        },
        {
            context: srcPath,
            from: 'assets/**/*',
            ignore: []
        },
        {
            context: path.join(contextPath, 'node_modules'),
            from: 'bxuip-angular.js-ui/dist/assets/**/*',
            to: path.join(contextPath, 'build/assets'),
            ignore: []
        }
    ],
    devServer: {
        host: 'localhost',
        port: 8089,
        proxy: {
            "/": {
                target: 'http://localhost:9100/eims'
            }
        }
    }
};
