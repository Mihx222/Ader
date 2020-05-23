<template>
  <div class="custom">
    <table>
      <thead>
      <tr>
        <th>Name</th>
      </tr>
      </thead>
      <tbody>
      <tr v-bind:key="category.name" v-for="category in this.text">
        <td>
          {{ category.name }}
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
    export default {
        props: {
            instance: { //<1>
                type: Object,
                required: true
            }
        },
        data: () => ({
            text: ''
        }),
        async created() {
            const response = await this.instance.axios.get('/actuator/category'); //<2>
            this.text = response.data;
        }
    };
</script>
