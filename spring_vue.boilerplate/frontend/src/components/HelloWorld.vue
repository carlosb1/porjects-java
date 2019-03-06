<template>
  <div class="hello">
	  <div class="container">
		<div>
			<div class="row">
				<div class="col-sm">
					<button class="btn btn-secondary" type="button" v-on:click="callRestService()" >Send message</button>
				</div>
			</div>
		</div>
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
		errors: []
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
