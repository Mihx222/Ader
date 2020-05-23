<template>
  <div class="container">
    <form @submit.prevent="addCategory" class="form-inline" id="categoryForm">
      <label for="categoryInput"><strong>Add new category: </strong></label>
      <input id="categoryInput" placeholder="Enter category name..." ref="categoryInput" type="text">
      <input class="button button-green" type="submit" value="Add">
    </form>
    <hr>
    <div style="overflow-x: auto;">
      <table id="categoryTable">
        <thead>
        <tr>
          <th class="header-spaced">Name</th>
          <th class="header-spaced">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-bind:key="category.name" v-for="category in this.categories">
          <td class="spaced underline">
            {{ category.name }}
          </td>
          <td class="underline">
            <button class="button button-red" v-on:click="deleteCategory(category.name)">
              Delete
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
    export default {
        props: {
            instance: {
                type: Object,
                required: true
            }
        },
        data: () => ({
            categories: ''
        }),
        async created() {
            const response = await this.instance.axios.get('/actuator/category');
            this.categories = response.data;
        },
        methods: {
            async deleteCategory(categoryName) {
                await this.instance.axios.delete('/actuator/category/' + categoryName);
                this.categories = this.categories.filter(category => category.name !== categoryName);
            },
            async addCategory() {
                let response = await this.instance.axios.post('/actuator/category/' + this.$refs.categoryInput.value);
                this.categories.push(response.data);
                console.log(this.categories);
            }
        }
    };
</script>

<style>
  .container {
    width: 80% !important;
  }

  #categoryTable {
    width: 100%;
  }

  .spaced {
    padding: 10px 0 0 10px;
    text-align: left !important;
  }

  .header-spaced {
    padding: 10px 0 10px 10px;
    text-align: left !important;
  }

  .underline {
    border-bottom: 1px solid #ddd;
  }

  #categoryTable tr:hover {
    background-color: #f5f5f5;
  }

  #categoryTable tr:nth-child(even) {
    background-color: #f2f2f2;
  }

  .button {
    background-color: #4CAF50; /* Green */
    border: none;
    color: white;
    padding: 16px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    transition-duration: 0.4s;
    cursor: pointer;
  }

  .button-red {
    background-color: white;
    color: black;
    border: 2px solid #f44336;
  }

  .button-red:hover {
    background-color: #f44336 !important;
    color: white !important;
  }

  .button-green {
    background-color: white;
    color: black;
    border: 2px solid #42d3a5;
  }

  .button-green:hover {
    background-color: #42d3a5 !important;
    color: white !important;
  }

  #categoryInput input[type=text], select {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
  }

  .form-inline {
    display: flex;
    flex-flow: row wrap;
    align-items: center;
  }

  #categoryForm .form-inline label {
    margin: 5px 10px 5px 0;
  }

  #categoryForm .form-inline input {
    vertical-align: middle;
    margin: 5px 10px 5px 0;
    padding: 10px;
    background-color: #fff;
    border: 1px solid #ddd;
  }

  #categoryForm .form-inline button {
    padding: 10px 20px;
    background-color: dodgerblue;
    border: 1px solid #ddd;
    color: white;
    cursor: pointer;
  }

  #categoryForm .form-inline button:hover {
    background-color: royalblue;
  }
</style>
