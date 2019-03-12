<template>
	<div class="hello">
		<div class="container">
			<b-list-group>
 
			<div v-if="results">
				<hr>
				<div class="msg-group p-2" v-for="result in results">
					<b-list-group-item button v-b-toggle.collapse1 >{{ result }}</b-list-group-item>
				<b-collapse id="collapse1" class="mt-2">
					<b-card>
					<p class="card-text">Collapse contents Here</p>
					This my content!
					</b-card>
				</b-collapse>	
				</div>
			</div>
			</b-list-group>
		</div>
	</div>
</template>

<script>
import {AXIOS} from './http-common'

export default {
	name: 'HelloWorld',
	props: {
		msg: String
	},
	data() {
		return {
			response: [],
			errors: [],
			results: ['link1', 'link2']
		}

	},
	methods: {
		callRestService: function() {
			AXIOS.get('/hello').then(
				response => {
					console.log(response.data)
					this.response = response.data
				}).catch(e => {
					this.errors.push(e)
				})
		},
		search: function() {
			AXIOS.get('/news').then(
				response => {
					console.log(response.data)
					this.response = response.data
				}).catch(e => {
					this.errors.push(e)
				});

		}
	}

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

h3 {
	margin: 40px 0 0;
}
ul {
	list-style-type: none;
	padding: 0;
}
li {
	display: inline-block;
	margin: 0 10px;
}
a {
	color: #42b983;
}
</style>
