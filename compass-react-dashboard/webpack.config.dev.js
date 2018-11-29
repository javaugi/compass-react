const path = require('path')
const webpack = require('webpack')
const fs = require('fs') // to check if the file exists
const dotenv = require('dotenv')
const ExtractTextPlugin = require('extract-text-webpack-plugin')

var production = process.argv.reduce(function(p, c){return p || c == '-p'}, false)


var config = {
  resolve: {
        root: [
            path.resolve(__dirname  + '/src')
        ],
        alias: {
            src: path.resolve(__dirname  + '/src');

            // Get the root path (assuming your webpack config is in the root of your project!)
            const currPath = path.join(__dirname);
            // Create the fallback path (the production .env)
            const basePath = currPath + '/.env';

            // We're concatenating the environment name to our filename to specify the correct env file!
            const envPath = basePath + '.' + env.ENVIRONMENT;

            // Set the path parameter in the dotenv config
            const env = dotenv.config({ path: finalPath }).parsed;

            // call dotenv and it will return an Object with a parsed key
            //const env = dotenv.config().parsed;
            const envKeys = Object.keys(env).reduce((prev, next) => {
              prev[`process.env.${next}`] = JSON.stringify(env[next]);
              return prev;
            }, {});
        }
    },

    context: path.join(__dirname, '/src'),
    entry: {
        app:'./index.js',
        vendor: [
            'babel-polyfill',
            'react', 'react-dom'
        ],
    },
    output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'dist'),
    publicPath: "/dist/"
   }
   module: {
    rules: [
      {
        test: /\.jsx?$/,
        exclude: /node_modules/,
        use: [{
          loader: "babel-loader",
          options: { presets: ['env', 'react']}
        }]
      }
    ]
  }

    plugins: [
        new ExtractTextPlugin(path.normalize('[name].css')),
        new webpack.optimize.CommonsChunkPlugin("vendor", 'vendor.js', Infinity),
        new webpack.DefinePlugin(envKeys)
    ],
    stats:{
        children: false
    },
    devServer: {
        quiet: false,
            noInfo: false,
            stats:{
            assets: false,
                colors: false,
                version: true,
                hash: true,
                timings: true,
                chunks: true,
                chunkModules: false,
                children: false
        }
    }
}


if(production){
    config.plugins.push(new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': JSON.stringify('production')
            }
        })
    )

    config.plugins.push(new webpack.optimize.UglifyJsPlugin(
        {
            compress: {
                warnings: false
            }
        })
    )
}

module.exports = config
