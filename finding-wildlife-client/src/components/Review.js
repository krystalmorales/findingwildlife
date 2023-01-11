import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import UserContext from "../UserContext";

function Review({review, trails, handleDelete}) {
    const history = useHistory();
    const authManager = useContext(UserContext);



    function confirmDelete(){
      if(window.confirm("Are you sure you want to Delete this Trail Review?")){
        handleDeleteTrailReview();
      }
    }

    // useEffect(() => {
    //     fetch(`http://localhost:8080/finding-wildlife/trail`)
    //     .then(response => {
    //       if(response.status === 200) {
    //         return response.json();
    //       }
    //       return Promise.reject('Something Went Wrong!');
    //     })
    //     .then(data => {
    //       setTrails(data);
    //     })
    //     .catch(error => history.push('/error', {errorMessage: error}));
    //   }, []);

    const handleDeleteTrailReview = () => {
      const init = {
        method: 'DELETE',
        headers: {
            Authorization: `Bearer ${authManager.user.token}`
        }
    };

    fetch(`http://localhost:8080/finding-wildlife/trail-review/${review.trailReviewId}/${review.appUserId}`, init)
    .then(response => {
        switch(response.status) {
            case 204:
                return null;
            case 404:
                history.push('./not-found')
                break;
            default:
                return Promise.reject('Something Went Wrong');
        }
    })
    .then(response => {
        if(!response) {
            handleDelete(review.trailReviewId)
        }
        else{
            console.log(response)
        }
    })
    .catch(error => history.push('./error', {errorMessage: error}))
      }
    
    const handleEditTrailReview = () => {
      history.push(`/trail-review/update/${review.trailReviewId}`);
      }

      function test() {
        for (let i =0; i < trails.length; i++) {
          if(trails[i].trailId === review.trailId) {
            return (<td>{trails[i].trailName}</td>)
          }
        }
      }

      return (
        <tr>
            <td>{review.rating}</td>
            <td>{review.difficulty}</td>
            <td>{review.comments}</td>
            {test()}
            <td>
                {authManager.user && <button type="button" className="btn btn-primary mr-3 mb-2" onClick={handleEditTrailReview}>Edit</button>}
                {authManager.user && <button type="button" className="btn btn-danger mb-2" onClick={confirmDelete}>Delete</button>}
            </td>
        </tr>
      )
}

export default Review;