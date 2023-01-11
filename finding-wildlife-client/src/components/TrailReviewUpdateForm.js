import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import UserContext from "../UserContext";
import Error from "./Error";

const DEFAULT_REVIEW = { trailId: '', rating: '', difficulty: '', comments: '' }

function TrailReviewUpdateForm(){

    const [trailReview, setTrailReview] = useState(DEFAULT_REVIEW);
    const [errors, setErrors] = useState([]);

    const history = useHistory();
    const { editId } = useParams();
    const authManager = useContext(UserContext);

    useEffect(() => {
        if(editId) {
            fetch(`http://localhost:8080/finding-wildlife/trail-review/trail-review-id/${editId}`)
            .then(response => {
                switch(response.status){
                    case 200:
                        return response.json();
                    default:
                        return Promise.reject('Something Went Wrong!');
                }
            })
            .then(body => {
                if(body){
                    setTrailReview(body[0]);
                }
            })
            .catch(error => history.push('/error', {errorMessage: error}));
        }
    }, []);

    function updateTrailReview(){
        const updateTrailReview = {id: editId, ...trailReview, appUserId: authManager.user.appUserId};

        const init = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${authManager.user.token}`
            },
            body: JSON.stringify(updateTrailReview)
        };

        fetch(`http://localhost:8080/finding-wildlife/trail-review/${editId}`, init)
        .then(response => {
            switch(response.status) {
                case 204:
                    return null;
                case 400:
                    return response.json();
                case 409:
                    history.push('/not-found', { id: editId} );
                    break;
                default:
                    return Promise.reject('Something Went Wrong!');
            }
        })
        .then(body => {
            if(!body) {
                history.push('/userPage')
            }
            else if(body){
                setErrors(body)
            }
        })
        .catch(error => history.push('./error', {errorMessage: error}));
    }

    function onSubmit(event){
        event.preventDefault();

        updateTrailReview();
    }

    function handleChange(event){
        const property = event.target.name;
        const value = event.target.value;

        const newTrailReview = {...trailReview};

        newTrailReview[property] = value;
        setTrailReview(newTrailReview);
    }

    function handleCancel(){
        history.push('/userPage')
    }

    return(<>
    <div className="update-review-container">
        <h2 className="text-center">Update Trail Review</h2>
        {errors.length > 0 ? <Error errors={errors}/> : null}
        <form onSubmit={onSubmit}>
            <div className="form-group">
                <label htmlFor="rating">Rating</label>
                <input name="rating" type="number" className="form-control" id="rating" value={trailReview.rating} onChange={handleChange}/>
            </div>
            <div className="form-group">
                <label htmlFor="difficulty">Difficulty</label>
                <input name="difficulty" type="number" className="form-control" id="difficulty" value={trailReview.difficulty} onChange={handleChange}/>
            </div>
            <div className="form-group">
                <label htmlFor="comments">Comments</label>
                <input name="comments" type="text" className="form-control mb-3" id="comments" value={trailReview.comments} onChange={handleChange}/>
            </div>
            <div className="form-group text-center">
                <button type="submit" className="btn btn-success">Submit</button>
                <button type="button" className="btn btn-danger" onClick={handleCancel}>Cancel</button>
            </div>
        </form>
        </div>
    </>);
}

export default TrailReviewUpdateForm;