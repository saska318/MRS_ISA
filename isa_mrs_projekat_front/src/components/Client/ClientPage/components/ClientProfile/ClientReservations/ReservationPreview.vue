<template>
  <div class="modal fade" :id="'bill-'+this.id" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header p-0 m-0" style="border-bottom: none">
        </div>
        <div class="modal-body pt-0 mt-0">
          <div class="row text-center pb-0 mb-0" style="border-bottom: 1px solid lightgray;">
            <h1 class="modal-title pb-1" style="color: #008970; font-weight: 700;">Receipt</h1>
          </div>

          <div v-if="hasSpecialOffer" class="p-0 m-0">
            <div class="row my-0 py-0 " v-for="(service, i) in specialOffer.includedServices" :key="i">
              <div class="col-md-9">
                <p class="h5">{{ service.name }}</p>
              </div>
              <div class="col-md-3 align-items-end" style="border-left: 1px solid lightgray;">
                <p class="h5">Free</p>
              </div>
            </div>
          </div>

          <div v-else class="p-0 m-0">
            <div class="row my-0 py-0 " v-for="(service, i) in services" :key="i">
              <div class="col-md-9">
                <p class="h5">{{ service.name }}</p>
              </div>
              <div class="col-md-3 align-items-end" style="border-left: 1px solid lightgray;">
                <p class="h5" style="font-weight: 800;">${{ service.price }}</p>
              </div>
            </div>
          </div>


          <div class="row my-0 py-0" style="border-top: 1px solid lightgray;">
            <div class="col-md-9">
              <p class="h5">Renting period:</p>
            </div>
            <div class="col-md-3 align-items-end" style="border-left: 1px solid lightgray;">
              <p class="h5">{{ this.days }}</p>
            </div>
          </div>
          <div class="row my-0 py-0">
            <div class="col-md-9">
              <p class="h5">Price:</p>
            </div>
            <div class="col-md-3 align-items-end" style="border-left: 1px solid lightgray;">
              <p class="h5">${{ this.price }}</p>
            </div>
          </div>
          <div class="row my-0 py-0" style="border-bottom: 1px solid lightgray; border-top: 1px solid lightgray;">
            <div class="col-md-9">
              <p class="h5">Total rent:</p>
            </div>
            <div class="col-md-3 align-items-end" style="border-left: 1px solid lightgray;">
              <p class="h5" style="font-weight: 800;">${{ calculateTotal }}</p>
            </div>
          </div>

          <div v-if="hasSpecialOffer" class="row my-0 py-0" style="border-bottom: 1px solid lightgray;">
            <div class="col-md-9">
              <p class="h5">Discount:</p>
            </div>
            <div class="col-md-3 align-items-end" style="border-left: 1px solid lightgray;">
              <p class="h5" style="font-weight: 800;">-{{ specialOffer.discount }}%</p>
            </div>
          </div>

          <div class="row my-0 py-0" style="color:#008970; font-weight: 700;">
            <div class="col-md-9" style="text-align: right;">
              <p class="h3"><strong>Total:</strong></p>
            </div>
            <div class="col-md-3 align-items-end">
              <p class="h3"><strong>${{ this.total }}</strong></p>
            </div>
          </div>
        </div>
        <div class="modal-footer" style="border-top: none;">
          <button type="button" class="btn btn-secondary w-100" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReservationPreview",
  props: ["services", "days", "price", "total", "id", "specialOffer"],
  computed: {
    calculateTotal() {
      return this.days*this.price;
    },
    hasSpecialOffer() {
      return this.specialOffer !== null;
    }
  },
  mounted() {
  },
}
</script>

<style scoped>

</style>