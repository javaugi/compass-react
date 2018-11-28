const path = require('path')
const webpack = require('webpack')
const ExtractTextPlugin = require('extract-text-webpack-plugin')

var production = process.argv.reduce(function(p, c){return p || c == '-p'}, false)


var config = {
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
        new webpack.optimize.CommonsChunkPlugin("vendor", 'vendor.js', Infinity)
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
