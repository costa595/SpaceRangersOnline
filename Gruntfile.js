module.exports = function (grunt) {

    grunt.initConfig({
        watch: {
            fest: {
                files: ['templates/*.xml'],
                tasks: ['fest'],
                options: {
                    atBegin: true,
                    livereload: true
                }
            },
            sass: {
                files: [
                  'public_html/css/sass/main.sass',
                  'public_html/css/sass/variables.sass',
                  'public_html/css/sass/app.sass',
                  'public_html/css/sass/game.sass',
                  'public_html/css/sass/createProfile.sass',
                  'public_html/css/sass/profile.sass',
                  'public_html/css/sass/toolbar.sass',
                  'public_html/css/sass/rating.sass',
                  'public_html/css/sass/baron.sass',
                  'public_html/css/sass/profileBarTop.sass',
                  'public_html/public_html/css/sass/fonts.sass'
                  
                ],
                tasks: ['sass'],
                options: {
                    atBegin: true,
                    livereload: true
                }
            },
            server: {
                files: [
                    'public_html/js/**/*.js',
                    'public_html/css/**/*.css'
                ],
                options: {
                    interrupt: true,
                    livereload: true
                }
            }
        },
        fest: {
            templates: {
                files: [{
                    expand: true,
                    cwd: 'templates',
                    src: '*.xml',
                    dest: 'public_html/js/tmpl'
                }],
                options: {
                    template: function (data) {
                        return grunt.template.process(
                            'define(function () { return <%= contents %> ; });',
                            {data: data}
                        );
                    }
                }
            }
        },
        sass: {
            style: "compressed",
            dist: {
                files: {
                    'public_html/css/main.css': 'public_html/css/sass/main.sass'
                }
            }
        },
        requirejs: {
            build: { /* Подзадача */
                options: {
                    almond: true,
                    baseUrl: "public_html/js",
                    mainConfigFile: "public_html/js/config.js",
                    name: "main",
                    optimize: "none",
                    out: "public_html/js/build/main.js"
                }
            },
            compile: {
                options: {
                    baseUrl: 'js',
                    mainConfigFile: 'public_html/js/config.js',
                    paths: {
                        'requireLib': 'require'
                    },
                    name: 'config',
                    out: 'public_html/build/dist.js',
                    include: ['requireLib'],
                    optimize: 'uglify'
                }
            }
        },
        concat: {
            build: { /* Подзадача */
                separator: ';\n',
                src: [
                      'public_html/js/lib/almond.js',
                      'public_html/js/build/main.js'
                ],
                dest: 'public_html/js/build.js'
            }
        },
        uglify: {
            build: { /* Подзадача */
                files: {
                    'public_html/js/build.min.js': 
                          ['public_html/js/build.js']
                }
            }
        },
    });

    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-fest');
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');
    
    grunt.registerTask('default', ['watch']);
    grunt.registerTask(
        'build',
        [
            'fest', 'requirejs:build',
            'concat:build', 'uglify:build'
        ]
    );
};
