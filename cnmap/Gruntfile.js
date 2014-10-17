// Generated on 2014-03-06 using generator-angular 0.7.1
'use strict';

// # Globbing
// for performance reasons we're only matching one level down:
// 'test/spec/{,*/}*.js'
// use this if you want to recursively match all subfolders:
// 'test/spec/**/*.js'

module.exports = function (grunt) {

  // Load grunt tasks automatically
  require('load-grunt-tasks')(grunt);

  // Time how long tasks take. Can help when optimizing build times
  require('time-grunt')(grunt);

  // Define the configuration for all the tasks
  grunt.initConfig({

    // Project settings
    yeoman: {
      // configurable paths
      app: require('./bower.json').appPath || 'app',
      dist: 'dist'
    },

    // Watches files for changes and runs tasks based on the changed files
    watch: {
      js: {
        files: ['<%= yeoman.app %>/scripts/{,*/}*.js'],
        tasks: ['newer:jshint:all'],
        options: {
          livereload: true
        }
      },
      jsTest: {
        files: ['test/spec/{,*/}*.js'],
        tasks: ['newer:jshint:test', 'karma']
      },
      compass: {
        files: ['<%= yeoman.app %>/styles/{,*/}*.{scss,sass}'],
        tasks: ['compass:server', 'autoprefixer']
      },
      gruntfile: {
        files: ['Gruntfile.js']
      },
      livereload: {
        options: {
          livereload: '<%= connect.options.livereload %>'
        },
        files: [
          '<%= yeoman.app %>/{,*/}*.html',
          '.tmp/styles/{,*/}*.css',
          '<%= yeoman.app %>/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}'
        ]
      }
    },

    // The actual grunt server settings
    connect: {
      options: {
        port: 9000,
        // Change this to '0.0.0.0' to access the server from outside.
        hostname: 'localhost',
        livereload: 35729
      },
      livereload: {
        options: {
          open: true,
          base: [
            '.tmp',
            '<%= yeoman.app %>'
          ]
        }
      },
      test: {
        options: {
          port: 9001,
          base: [
            '.tmp',
            'test',
            '<%= yeoman.app %>'
          ]
        }
      },
      dist: {
        options: {
          base: '<%= yeoman.dist %>'
        }
      }
    },

    // Make sure code styles are up to par and there are no obvious mistakes
    jshint: {
      options: {
        jshintrc: '.jshintrc',
        reporter: require('jshint-stylish')
      },
      all: [
        'Gruntfile.js',
        '<%= yeoman.app %>/scripts/{,*/}*.js'
      ],
      test: {
        options: {
          jshintrc: 'test/.jshintrc'
        },
        src: ['test/spec/{,*/}*.js']
      }
    },

    // Empties folders to start fresh
    clean: {
      dist: {
        files: [{
          dot: true,
          src: [
            '.tmp',
            '<%= yeoman.dist %>/*',
            '!<%= yeoman.dist %>/.git*'
          ]
        }]
      },
      server: '.tmp'
    },

    // Add vendor prefixed styles
    autoprefixer: {
      options: {
        browsers: ['last 1 version']
      },
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '{,*/}*.css',
          dest: '.tmp/styles/'
        }]
      }
    },

    // Automatically inject Bower components into the app
    'bower-install': {
      app: {
        html: '<%= yeoman.app %>/index.html',
        ignorePath: '<%= yeoman.app %>/'
      }
    },




    // Compiles Sass to CSS and generates necessary files if requested
    compass: {
      options: {
        sassDir: '<%= yeoman.app %>/styles',
        cssDir: '.tmp/styles',
        generatedImagesDir: '.tmp/images/generated',
        imagesDir: '<%= yeoman.app %>/images',
        javascriptsDir: '<%= yeoman.app %>/scripts',
        fontsDir: '<%= yeoman.app %>/styles/fonts',
        importPath: '<%= yeoman.app %>/bower_components',
        httpImagesPath: '/images',
        httpGeneratedImagesPath: '/images/generated',
        httpFontsPath: '/styles/fonts',
        relativeAssets: false,
        assetCacheBuster: false,
        raw: 'Sass::Script::Number.precision = 10\n'
      },
      dist: {
        options: {
          generatedImagesDir: '<%= yeoman.dist %>/images/generated'
        }
      },
      server: {
        options: {
          debugInfo: true
        }
      }
    },

    // Renames files for browser caching purposes
    rev: {
      dist: {
        files: {
          src: [
            '<%= yeoman.dist %>/scripts/{,*/}*.js',
            '<%= yeoman.dist %>/styles/{,*/}*.css',
            '<%= yeoman.dist %>/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}',
            '<%= yeoman.dist %>/styles/fonts/*'
          ]
        }
      }
    },

    // Reads HTML for usemin blocks to enable smart builds that automatically
    // concat, minify and revision files. Creates configurations in memory so
    // additional tasks can operate on them
    useminPrepare: {
      html: '<%= yeoman.app %>/index.html',
      options: {
        dest: '<%= yeoman.dist %>'
      }
    },

    // Performs rewrites based on rev and the useminPrepare configuration
    usemin: {
      html: ['<%= yeoman.dist %>/{,*/}*.html'],
      css: ['<%= yeoman.dist %>/styles/{,*/}*.css'],
      options: {
        assetsDirs: ['<%= yeoman.dist %>']
      }
    },

    // The following *-min tasks produce minified files in the dist folder
    imagemin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '{,*/}*.{png,jpg,jpeg,gif}',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },
    svgmin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '{,*/}*.svg',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },
    htmlmin: {
      dist: {
        options: {
          collapseWhitespace: true,
          collapseBooleanAttributes: true,
          removeCommentsFromCDATA: true,
          removeOptionalTags: true
        },
        files: [{
          expand: true,
          cwd: '<%= yeoman.dist %>',
          src: ['*.html', 'views/{,*/}*.html'],
          dest: '<%= yeoman.dist %>'
        }]
      }
    },

    // Allow the use of non-minsafe AngularJS files. Automatically makes it
    // minsafe compatible so Uglify does not destroy the ng references
    ngmin: {
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/concat/scripts',
          src: '*.js',
          dest: '.tmp/concat/scripts'
        }]
      }
    },

    // Replace Google CDN references
    cdnify: {
      dist: {
        html: ['<%= yeoman.dist %>/*.html']
      }
    },

    // Copies remaining files to places other tasks can use
    copy: {
      dist: {
        files: [{
          expand: true,
          dot: true,
          cwd: '<%= yeoman.app %>',
          dest: '<%= yeoman.dist %>',
          src: [
            '*.{ico,png,txt}',
            '.htaccess',
            '*.html',
            'views/{,*/}*.html',
            'bower_components/**/*',
            'images/{,*/}*.{webp}',
            'fonts/*'
          ]
        }, {
          expand: true,
          cwd: '.tmp/images',
          dest: '<%= yeoman.dist %>/images',
          src: ['generated/*']
        }]
      },
      styles: {
        expand: true,
        cwd: '<%= yeoman.app %>/styles',
        dest: '.tmp/styles/',
        src: '{,*/}*.css'
      }
    },

    // Run some tasks in parallel to speed up the build process
    concurrent: {
      server: [
        'compass:server'
      ],
      test: [
        'compass'
      ],
      dist: [
        'compass:dist',
        'imagemin',
        'svgmin'
      ]
    },

    // By default, your `index.html`'s <!-- Usemin block --> will take care of
    // minification. These next options are pre-configured if you do not wish
    // to use the Usemin blocks.
    cssmin: {
       dist: {
         files: {
           '<%= yeoman.app %>/styles/main.min.css': [
             '<%= yeoman.app %>/styles/main.css'
           ]
//           ,'<%= yeoman.app %>/styles/vendor.min.css': [
//                 '<%= yeoman.app %>/bower_components/angular-xeditable/dist/css/xeditable.css',
//                 '<%= yeoman.app %>/bower_components/blueimp-file-upload/css/jquery.fileupload.css',
//                 '<%= yeoman.app %>/bower_components/Jcrop/css/jquery.Jcrop.css',
//                 '<%= yeoman.app %>/bower_components/blueimp-gallery/css/blueimp-gallery-indicator.css'
//                 ,'<%= yeoman.app %>/bower_components/perfect-scrollbar/src/perfect-scrollbar.css'
//                 ,'<%= yeoman.app %>/bower_components/angular-bootstrap-datetimepicker/src/css/datetimepicker.css'
//             ]
         }
       }
    },
    uglify: {
        options: {
            mangle: {
                except: ['jQuery', 'Backbone']
            }
        },
       dist: {
           options: {
//               beautify: true
           },
         files: {
             '<%= yeoman.app %>/scripts/ponmApp.min.js': [
                 '<%= yeoman.app %>/scripts/app.js',
                 '<%= yeoman.app %>/scripts/controllers.js',
                 '<%= yeoman.app %>/scripts/directives.js',
                 '<%= yeoman.app %>/scripts/services.js',
                 '<%= yeoman.app %>/scripts/services/*.js'
             ],
           '<%= yeoman.app %>/scripts/ponmApp.directives.min.js': [
               '<%= yeoman.app %>/scripts/directives/*.js'
               ],
           '<%= yeoman.app %>/scripts/panor/scripts.min.js': [
//               '<%= yeoman.app %>/scripts/panor/MapUtils.js',
//               '<%= yeoman.app %>/scripts/panor/MapTravel.js',
               '<%= yeoman.app %>/scripts/panor/Map*.js',
               '<%= yeoman.app %>/scripts/panor/panoramio/cnmap.comm.js',
               '<%= yeoman.app %>/scripts/panor/panoramio/cnmap.Panoramio.js'
             ],
           '<%= yeoman.app %>/scripts/panor/scripts.google.min.js': [
             '<%= yeoman.app %>/scripts/panor/google/Map*.js',
             '<%= yeoman.app %>/scripts/panor/panoramio/cnmap.google.js'
           ],
           '<%= yeoman.app %>/scripts/panor/scripts.gaode.min.js': [
             '<%= yeoman.app %>/scripts/panor/gaode/Map*.js',
             '<%= yeoman.app %>/scripts/panor/panoramio/cnmap.gaode.js'
           ],
           '<%= yeoman.app %>/scripts/panor/scripts.qq.min.js': [
               '<%= yeoman.app %>/scripts/panor/qq/Map*.js',
               '<%= yeoman.app %>/scripts/panor/panoramio/cnmap.qq.js'
             ],
           '<%= yeoman.app %>/scripts/panor/scripts.baidu.min.js': [
               '<%= yeoman.app %>/scripts/panor/baidu/*.js',
               '<%= yeoman.app %>/scripts/panor/panoramio/cnmap.baidu.js'
             ],
           '<%= yeoman.app %>/scripts/ponmApp.controllers.min.js': [
                 '<%= yeoman.app %>/scripts/controllers/*Ctrl.js'
             ]
//             ,
//           '<%= yeoman.app %>/scripts/blueimp-file-upload.min.js': [
//               '<%= yeoman.app %>/bower_components/blueimp-file-upload/js/vendor/jquery.ui.widget.js',
//               '<%= yeoman.app %>/bower_components/blueimp-load-image/js/load-image.js',
//               '<%= yeoman.app %>/bower_components/blueimp-load-image/js/load-image-meta.js',
//               '<%= yeoman.app %>/bower_components/blueimp-load-image/js/load-image-exif.js',
//               '<%= yeoman.app %>/bower_components/blueimp-load-image/js/load-image-exif-map.js',
//               '<%= yeoman.app %>/bower_components/blueimp-load-image/js/load-image-ios.js',
//               '<%= yeoman.app %>/bower_components/blueimp-load-image/js/load-image-orientation.js',
//               '<%= yeoman.app %>/bower_components/blueimp-canvas-to-blob/js/canvas-to-blob.min.js',
//               '<%= yeoman.app %>/bower_components/blueimp-gallery/js/jquery.blueimp-gallery.min.js',
//               '<%= yeoman.app %>/bower_components/blueimp-file-upload/js/jquery.iframe-transport.js',
//               '<%= yeoman.app %>/bower_components/blueimp-file-upload/js/jquery.fileupload.js',
//               '<%= yeoman.app %>/bower_components/blueimp-file-upload/js/jquery.fileupload-process.js',
//               '<%= yeoman.app %>/bower_components/blueimp-file-upload/js/jquery.fileupload-image.js',
//               '<%= yeoman.app %>/bower_components/blueimp-file-upload/js/jquery.fileupload-audio.js',
//               '<%= yeoman.app %>/bower_components/blueimp-file-upload/js/jquery.fileupload-video.js',
//               '<%= yeoman.app %>/bower_components/blueimp-file-upload/js/jquery.fileupload-validate.js',
//               '<%= yeoman.app %>/bower_components/blueimp-file-upload/js/jquery.fileupload-angular.js'
//             ]
//             ,
//             '<%= yeoman.app %>/scripts/ponmApp.vendor.min.js': [
//                 '<%= yeoman.app %>/bower_components/jquery/dist/jquery.js',
//                 '<%= yeoman.app %>/bower_components/jquery.rest/dist/jquery.rest.min.js',
//                 '<%= yeoman.app %>/bower_components/bootstrap-sass-official/assets/javascripts/bootstrap.js',
//
//                 '<%= yeoman.app %>/bower_components/angular/angular.min.js',
//                 '<%= yeoman.app %>/bower_components/angular-route/angular-route.min.js',
//                 '<%= yeoman.app %>/bower_components/angular-resource/angular-resource.min.js',
//                 '<%= yeoman.app %>/bower_components/angular-cookies/angular-cookies.min.js',
//                 '<%= yeoman.app %>/bower_components/angular-sanitize/angular-sanitize.min.js',
//                 '<%= yeoman.app %>/bower_components/angular-animate/angular-animate.min.js',
//
//                 '<%= yeoman.app %>/bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
//                 '<%= yeoman.app %>/bower_components/angular-xeditable/dist/js/xeditable.min.js',
//                 '<%= yeoman.app %>/bower_components/angular-ui-utils/ui-utils.min.js',
//                 '<%= yeoman.app %>/bower_components/angular-ui-router/release/angular-ui-router.js',
//                 '<%= yeoman.app %>/bower_components/angular-dragdrop/src/angular-dragdrop.js',
//                 '<%= yeoman.app %>/bower_components/jquery-ui/jquery-ui.js',
//                 '<%= yeoman.app %>/bower_components/angular-local-storage/angular-local-storage.js',
//                 '<%= yeoman.app %>/bower_components/angular-scroll/angular-scroll.js',
//                 '<%= yeoman.app %>/bower_components/moment/moment.js',
//                 '<%= yeoman.app %>/bower_components/angular-bootstrap-datetimepicker/src/js/datetimepicker.js',
//
////                 '<%= yeoman.app %>/bower_components/jquery-bridget/jquery.bridget.js',
////                 '<%= yeoman.app %>/bower_components/get-style-property/get-style-property.js',
////                 '<%= yeoman.app %>/bower_components/get-size/get-size.js',
//                 '<%= yeoman.app %>/bower_components/eventie/eventie.js',
//                 '<%= yeoman.app %>/bower_components/eventEmitter/EventEmitter.min.js',
////                 '<%= yeoman.app %>/bower_components/doc-ready/doc-ready.js',
////                 '<%= yeoman.app %>/bower_components/matches-selector/matches-selector.js',
////                 '<%= yeoman.app %>/bower_components/outlayer/item.js',
////                 '<%= yeoman.app %>/bower_components/outlayer/outlayer.js',
////                 '<%= yeoman.app %>/bower_components/masonry/masonry.js',
//                 '<%= yeoman.app %>/bower_components/imagesloaded/imagesloaded.js',
////                 '<%= yeoman.app %>/bower_components/angular-masonry/angular-masonry.js',
//
//                 '<%= yeoman.app %>/bower_components/jquery-backstretch/jquery.backstretch.min.js',
//                 '<%= yeoman.app %>/bower_components/jquery-waypoints/waypoints.min.js',
//                 '<%= yeoman.app %>/bower_components/Jcrop/js/jquery.Jcrop.min.js',
//                 '<%= yeoman.app %>/bower_components/perfect-scrollbar/src/perfect-scrollbar.js',
//                 '<%= yeoman.app %>/bower_components/angular-perfect-scrollbar/src/angular-perfect-scrollbar.js',
//                 '<%= yeoman.app %>/bower_components/jquery-plugins/jquery-collagePlus/jquery.collagePlus.js',
//                 '<%= yeoman.app %>/bower_components/jquery-plugins/jquery-collagePlus/extras/jquery.removeWhitespace.js',
//                 '<%= yeoman.app %>/scripts/panor/js/color-thief.js',
//                 // image gallery
//                 '<%= yeoman.app %>/bower_components/blueimp-gallery/js/blueimp-helper.js',
//                 '<%= yeoman.app %>/bower_components/blueimp-gallery/js/blueimp-gallery.js',
//                 '<%= yeoman.app %>/bower_components/blueimp-gallery/js/blueimp-gallery-indicator.js',
//                 '<%= yeoman.app %>/bower_components/blueimp-gallery/js/blueimp-gallery-fullscreen.js',
//
//                 '<%= yeoman.app %>/scripts/panor/js/jquery.mousewheel.js',
//                 '<%= yeoman.app %>/scripts/panor/js/jquery.panzoom.js',
//                 '<%= yeoman.app %>/scripts/panor/js/three.min.js'
//             ]
         }
       }
    },
    concat: {
       dist: {}
    },

    // Test settings
    karma: {
      unit: {
        configFile: 'karma.conf.js',
        singleRun: true
      }
    }
  });


  grunt.registerTask('serve', function (target) {
    if (target === 'dist') {
      return grunt.task.run(['build', 'connect:dist:keepalive']);
    }

    grunt.task.run([
      'clean:server',
      'bower-install',
      'concurrent:server',
      'autoprefixer',
      'connect:livereload',
      'watch'
    ]);
  });

  grunt.registerTask('server', function () {
    grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
    grunt.task.run(['serve']);
  });

  grunt.registerTask('test', [
    'clean:server',
    'concurrent:test',
    'autoprefixer',
    'connect:test',
    'karma'
  ]);

  grunt.registerTask('build', [
    'clean:dist',
    'bower-install',
    'useminPrepare',
    'concurrent:dist',
    'autoprefixer',
    'concat',
    'ngmin',
    'copy:dist',
    'cdnify',
    'cssmin',
    'uglify',
    'rev',
    'usemin',
    'htmlmin'
  ]);

  grunt.registerTask('default', [
    'newer:jshint',
    'test',
    'build'
  ]);
};
