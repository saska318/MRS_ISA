<template>
  <div class="row">
    <div class="col-1"></div>
    <div class="col-10 pt-5 mb-5">
      <div class="container-fluid pe-0 ps-0 me-0 ms-0">
        <div class="align-items-center">
          <div class="row main-col mb-1">
            <div class="d-flex justify-content-start p-0 m-0">
              <div class="me-1">
                <label for="UserType">Author type</label>
                <select class="form-control me-1" id="UserType" v-model="filterAuthor" style="max-width: 400px; min-width: fit-content; width: 25vh" @change="filterActive=false">
                  <option value="RentalObjectOwner" id="authorOwner">Owner</option>
                  <option value="Client" id="authorClient">Client</option>
                </select>
              </div>
              <div class="mt-auto p-0">
                <button @click="filterButtonClicked" class="btn rounded" :class="filterActive?'btn-red':''" :disabled="filterAuthor===null"><font-awesome-icon :icon="filterActive?'x':'filter'"></font-awesome-icon></button>
              </div>
            </div>
          </div>
          <div class="row main-col text-center header rounded mb-3">
            <table>
              <thead>
              <tr>
                <th>Author</th>
                <th>Author type</th>
                <th>Comment</th>
                <th>Subject</th>
                <th></th>
                <th></th>
              </tr>
              </thead>
              <tbody>
              <tr class="p-1" v-for="(report, index) in this.reports" :key="index" :class="index%2!==0?'odd':'even'">
                <td>
                  {{report.author.name + " " + report.author.surname}}
                </td>
                <td>
                  {{this.userRole(report.author)}}
                </td>
                <td>
                  {{report.comment}}
                </td>
                <td>
                  {{report.subjectName}}
                </td>
                <td v-if="report.author.userType==='Client'" colspan="2">
                  <div class="dropdown d-flex justify-content-start">
                    <button class="btn dropdown" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
                      <font-awesome-icon icon="paper-plane"></font-awesome-icon>Reply
                    </button>
                    <div class="dropdown-menu form">
                      <div class="container-fluid" style="background-color: #f7f7f2">
                        <div class="row">
                          <p>Please enter the response (50-200 characters):</p>
                        </div>
                        <div class="row">
                          <form>
                            <div class="form-group">
                              <textarea class="form-control" style="resize: none" rows="3" maxlength="200" v-model="response"></textarea>
                            </div>
                            <div class="form-group text-center mt-1">
                              <button class="btn" :class="response!==null?null:'disabled'" @click.prevent="manageReport(report.id, true, response)">Confirm</button>
                            </div>
                          </form>
                        </div>
                      </div>
                    </div>
                  </div>
                </td>
                <td v-if="report.author.userType!=='Client'" class="text-end">
                  <button class="btn" data-bs-toggle="modal" data-bs-target="#confirmationDialog">
                    <font-awesome-icon icon="check"></font-awesome-icon>
                  </button>
                  <div class="modal fade" id="confirmationDialog" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h5 class="modal-title" id="exampleModalLabel">Report Management</h5>
                          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                          Do you want to accept {{report.author.name + " " + report.author.surname}}'s report for {{report.subjectName}}?
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-red" style="margin-right: 2vh;" data-bs-dismiss="modal">No</button>
                          <button type="button" class="btn" @click="manageReport(report.id, true)" data-bs-dismiss="modal">Yes</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </td>
                <td v-if="report.author.userType!=='Client'" class="text-start">
                  <button class="btn btn-red" data-bs-toggle="modal" data-bs-target="#confirmationDialogReject">
                    <font-awesome-icon icon="ban"></font-awesome-icon>
                  </button>
                  <div class="modal fade" id="confirmationDialogReject" tabindex="-1" aria-labelledby="RejectDialog" aria-hidden="true">
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h5 class="modal-title" id="RejectDialog">Report Management</h5>
                          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                          Do you want to reject {{report.author.name + " " + report.author.surname}}'s report for {{report.subjectName}}?
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-red" style="margin-right: 2vh;" data-bs-dismiss="modal">No</button>
                          <button type="button" class="btn" @click="manageReport(report.id, false)" data-bs-dismiss="modal">Yes</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
            <nav aria-label="Page navigation">
              <ul class="pagination justify-content-center mt-3">
                <li class="page-item" v-if="totalPages > 1"><button class="page-link" :disabled="currentPage===0" @click="previousPage">Previous</button></li>
                <li class="page-item mt-auto me-1 ms-1" v-if="currentPage > 1 && totalPages > 3">...</li>
                <li class="page-item"><button class="page-link" :disabled="currentPage === 0" @click="numberedPage(1)">{{button1Content}}</button></li>
                <li class="page-item" v-if="totalPages > 1"><button class="page-link" :disabled="currentPage !== 0 && totalPages - currentPage > 1" @click="numberedPage(2)">{{button2Content}}</button></li>
                <li class="page-item" v-if="totalPages > 2"><button class="page-link" :disabled="totalPages - currentPage === 1" @click="numberedPage(3)">{{button3Content}}</button></li>
                <li class="page-item mt-auto me-1 ms-1" v-if="totalPages - currentPage > 1 && totalPages > 3 && button3Content !== totalPages">...</li>
                <li class="page-item" v-if="totalPages > 1"><button class="page-link" :disabled="totalPages - currentPage === 1" @click="nextPage">Next</button></li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
      <div class="col-1"></div>
    </div>
  </div>
</template>

<script>
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import { library } from "@fortawesome/fontawesome-svg-core";
import {faFilter, faX, faCheck, faBan, faPaperPlane} from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import {useStore} from "vuex";
import {toggleProcessing} from "@/components/state";

library.add(faFilter, faX, faCheck, faBan, faPaperPlane);

export default {
  name: "AdminReportsView",
  components: {FontAwesomeIcon},
  data() {
    return {
      reports: [],
      currentPage: 0,
      totalPages: null,
      pageSize: 10,
      filterAuthor: null,
      filterActive: false,
      response: null
    }
  },
  mounted() {
    const store = useStore();
    axios.get("/Reports/getReports", {headers: {
        Authorization: "Bearer " + store.getters.access_token,
      },
      params: {
        page: this.currentPage,
        pageSize: this.pageSize
      }
    })
        .then(response => {
          this.reports = response.data.content;
          this.currentPage = response.data.currentPage;
          this.totalPages = response.data.pages;
        })
        .catch(() =>{
          this.$notify({
            title: "Server error",
            text: "Server is currently off. Please try again later...",
            type: "error"
          });
        });
  },
  methods: {
    manageReport(reportId, accepted, response) {
      toggleProcessing();
      response = response===undefined?"":response;
      console.log("response: '" + response +"'");
      axios.put("/Reports/manageReport", null,{
        headers: {
          Authorization: "Bearer " + this.$store.getters.access_token,
        },
        params: {
          id: reportId,
          accepted: accepted,
          response: response
        }
      })
          .then((response) => {
            this.$notify( {
              title: "Successfully managed",
              text: "You have successfully" + (accepted?"accepted":"rejected") + " the report with id " + response.data.id + ".",
              position: "bottom right",
              type: "success"
            });
            this.refreshPage();
            toggleProcessing();
          })
          .catch((error) => {
            if (!error.response) {
              this.$notify({
                title: "Server error",
                text: "Server is currently off. Please try again later...",
                type: "error"
              });
            } else if (error.response.status === 500) {
              this.$notify({
                title: "Internal Server Error",
                text: "Something went wrong on the server! Please try again later...",
                position: "bottom right",
                type: "error"
              });
            } else if (error.response.status === 409) {
              this.$notify({
                title: "Conflict",
                text: "We are sorry, but another administrator has already managed the report.",
                position: "bottom right",
                type: "warn"
              });
              this.refreshPage();
            }
            toggleProcessing();
          })
    },
    filterButtonClicked() {
      if (this.filterActive) {
        this.filterAuthor = null
        this.filterActive = !this.filterActive;
        this.refreshPage();
      } else if (this.filterAuthor !== null) {
        this.filterActive = !this.filterActive;
        this.refreshPage();
      }
    },
    userRole(user) {
      if (user.userType === "Instructor") {
        return "Fishing Instructor";
      } else if (user.userType === "VacationRentalOwner") {
        return "House Owner";
      } else if (user.userType === "BoatOwner") {
        return "Boat Owner";
      } else if (user.userType === "SuperAdmin") {
        return "Supreme Admin";
      } else {
        return user.userType;
      }
    },
    previousPage() {
      this.currentPage -= 1;
      this.refreshPage();
    },
    nextPage() {
      this.currentPage += 1;
      this.refreshPage();
    },
    numberedPage(buttonNumber) {
      if (buttonNumber === 1) {
        this.currentPage = this.button1Content - 1;
      } else if (buttonNumber === 2) {
        this.currentPage = this.button2Content - 1;
      } else if (buttonNumber === 3) {
        this.currentPage = this.button3Content - 1;
      }
      this.refreshPage();
    },
    refreshPage() {
      let params;
      let url;
      if (this.filterAuthor !== null) {
        params = {
          page: this.currentPage,
          pageSize: this.pageSize,
          userType: this.filterAuthor,
        };
        url = "/Reports/getReportsFilter";
      } else {
        params = {
          page: this.currentPage,
          pageSize: this.pageSize
        };
        url = "/Reports/getReports";
      }
      axios.get(url, {headers: {
          Authorization: "Bearer " + this.$store.getters.access_token,
        },
        params
      })
          .then(response => {
            this.reports = response.data.content;
            this.currentPage = response.data.currentPage;
            this.totalPages = response.data.pages;
          })
          .catch(() =>{
            this.$notify({
              title: "Server error",
              text: "Server is currently off. Please try again later...",
              type: "error"
            });
          });
    }
  },
  computed: {
    button1Content() {
      return this.currentPage === 0 ? this.currentPage+1: this.totalPages - this.currentPage === 1 ? this.currentPage - 1 : this.currentPage;
    },
    button2Content() {
      return this.currentPage === 0 ? this.currentPage+2: this.totalPages - this.currentPage === 1 ? this.currentPage : this.currentPage+1;
    },
    button3Content() {
      return this.currentPage === 0 ? this.currentPage+3: this.totalPages - this.currentPage === 1 ? this.currentPage + 1 : this.currentPage+2;
    },
  }
}
</script>

<style scoped>
label {
  color: #008970;
}
</style>