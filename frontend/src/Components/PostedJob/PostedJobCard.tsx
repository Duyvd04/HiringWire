import { Link, useParams } from "react-router-dom";
import { timeAgo } from "../../Services/Utilities";

const PostedJobCard=(props:any)=>{
    const {id}=useParams();
    return <Link data-aos="fade-up" to={`/posted-jobs/${props.id}`} className={` rounded-xl p-2 w-52 lg-mx:w-48 bs-mx:w-44 border-l-2 hover:bg-opacity-80 cursor-pointer border-l-oceanTeal-400 ${props.id==id?"bg-oceanTeal-400 text-black":"bg-deepSlate-100 text-deepSlate-300"}`}>
        <div className={`text-sm  font-semibold`}>{props.jobTitle}</div>
        <div className="text-xs  font-medium">{props.location}</div>
        <div className="text-xs">{props.jobStatus=="DRAFT"?"Drafted":props.jobStatus=="CLOSED"?"Closed":"Posted"} {timeAgo(props.postTime)}</div>
    </Link>
}
export default PostedJobCard;