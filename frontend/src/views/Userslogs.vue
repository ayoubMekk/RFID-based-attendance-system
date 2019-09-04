<template>
    <div class="projects">
        <h1 class="mx-2 my-2 subheading grey--text">Users logs</h1>
        <v-container v-if="logs !== null" class="my-5">
            <v-layout row justify-space-between class="mb-3  pa-1">
                <v-flex xs12 sm3 md1>
                    <v-checkbox
                            v-model="selected"
                            label="in"
                            color="success"
                            value="IN"
                    ></v-checkbox>
                </v-flex>
                <v-flex xs12 sm3 md1>
                    <v-checkbox
                            v-model="selected"
                            label="out"
                            color="red"
                            value="OUT"
                    ></v-checkbox>
                </v-flex>
                <v-flex xs12 sm3 md1>
                    <v-checkbox
                            v-model="selected"
                            label="banned"
                            color="grey darken-2"
                            value="Banned"
                    ></v-checkbox>
                </v-flex>
                <v-spacer></v-spacer>
                <v-flex xs12 sm3 md3>
                    <v-text-field
                            label="search by user"
                            v-model="search"
                    ></v-text-field>
                </v-flex>
            </v-layout>
            <v-card  class="my-3">
                <v-data-table
                        :headers="headers"
                        :items="getLogs"
                        :search="search"
                        :loading="false"
                >
                    <v-progress-linear v-slot:progress color="blue" indeterminate></v-progress-linear>
                    <template v-slot:items="props">
                        <td class="text-xs-left">{{ props.item.id }}</td>
                        <td class="text-xs-left">{{ props.item.name }}</td>
                        <td class="text-xs-left">{{ props.item.date }}</td>
                        <td class="text-xs-left">
                       
                            <v-chip small :class="`${props.item.status.toLowerCase()} white--text my-2 caption`">{{ props.item.status }}</v-chip>
                        </td>
                    </template>
                    <template v-slot:no-results>
                        <v-alert :value="true" color="error" icon="warning">
                            Your search for "{{ search }}" found no results.
                        </v-alert>
                    </template>
                </v-data-table>
            </v-card>
        </v-container>
        <template v-else>
            <v-alert
                    :value="true"
                    color="info"
                    icon="info"
                    outline
            >
                no logs exists for this user!
            </v-alert>
        </template>
    </div>
</template>

<script>
    import format from 'date-fns/format'
     import Pusher from 'pusher-js'
      import axios from 'axios'
    export default {
        data() {
            return {
                logs: [],
                id : 1,
                usersLogs: [],
                search: '',
                selected: ['IN', 'OUT', 'Banned'],
                banned: true,
                searchDate: null,
                menu: false,
                // url: 'http://192.168.43.151:8082/',
                url: 'http://localhost:8082/',
                headers: [
                    {
                        text: '#ID',
                        align: 'left',
                        sortable: true,
                        value: 'id'
                    },
                    {
                        text: 'User',
                        align: 'left',
                        sortable: true,
                        value: 'user'
                    },
                    {text: 'date', value: 'Date', sortable: true},
                    {text: 'status', value: 'Status'}
                ],
            }
        },
        computed:{

            getLogs(){
                 //   return this.logs
                
               return this.logs.filter((log) => {
                console.log(log.name)
                   return this.selected.includes(log.status) 
                })
                
            },
            formattedDate () {
                return this.searchDate ? format(this.searchDate, 'Do MMM YYYY') : ''
            }
        },
        methods: {

        },
        created() {

              this.$http.get(this.url +  'all-logs')
                .then(response => {
                    this.logs = response.data
                   // console.log(this.logs)
                });


            var pusher = new Pusher('84bee67aad46ed497369', {
                cluster: 'eu',
                forceTLS: true
            });

            let iduser = this.$route.params.id
            var channel = pusher.subscribe('my-channel');
            channel.bind('my-event', (data) => {
                  
                axios.get(this.url +  'all-logs')
                .then(response => (
            
                    this.logs = response.data
                ))
      
             });
          

            this.$http.get(this.url +  'executeRead/')


        }
    }
</script>
<style>
    .v-chip.in{
        background: #00897b;
    }
    .v-chip.out{
        background: #f44336;
    }
    .v-chip.banned {
        background: #616161 ;
    }
</style>
