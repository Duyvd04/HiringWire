import { Avatar, TextInput } from "@mantine/core";
import { IconSearch } from "@tabler/icons-react";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { updateFilter } from "../../Slices/FilterSlice";
import { useNavigate } from "react-router-dom";

const DreamJob = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [jobTitle, setJobTitle] = useState("");
    const [type, setType] = useState("");

    const handleClick = () => {
        dispatch(
            updateFilter({
                "Job Title": jobTitle ? [jobTitle] : null,
                "Job Type": type ? [type] : null,
                page: 1,
            })
        );
        navigate("/find-jobs");
    };

    return (
        <div className="min-h-screen flex flex-col items-center justify-center px-16 bs-mx:px-10 md-mx:px-5 bg-white sm-mx:flex-col sm-mx:gap-8">
            <div className="flex flex-col sm-mx:flex-col items-center justify-center w-full max-w-4xl gap-8 sm-mx:gap-6">
                {/* Left Section: Text and Inputs */}
                <div
                    data-aos="zoom-out-right"
                    className="flex flex-col w-full sm-mx:w-full gap-3 text-center sm-mx:text-center"
                >
                    <div className="text-6xl bs-mx:text-5xl md-mx:text-4xl sm-mx:text-3xl font-bold leading-tight text-deepSlate-900 [&>span]:text-oceanTeal-500">
                        One step away from your <span>dream</span> <span>job</span>
                    </div>
                    <div className="text-lg md-mx:text-base sm-mx:text-sm text-deepSlate-600">
                        Right job, right way — start today.
                    </div>
                    <div className="flex gap-3 mt-5 items-center justify-center flex-wrap">
                        <TextInput
                            value={jobTitle}
                            onChange={(e) => setJobTitle(e.currentTarget.value)}
                            className="bg-deepSlate-100 rounded-lg p-1 px-2 text-deepSlate-900 [&_input]:!text-deepSlate-900 w-64 sm-mx:w-full"
                            variant="unstyled"
                            label="Job Title"
                            placeholder="Software Engineer"
                        />
                        <TextInput
                            value={type}
                            onChange={(e) => setType(e.currentTarget.value)}
                            className="bg-deepSlate-100 rounded-lg p-1 px-2 text-deepSlate-900 [&_input]:!text-deepSlate-900 w-64 sm-mx:w-full"
                            variant="unstyled"
                            label="Job Type"
                            placeholder="Fulltime"
                        />
                        <div className="flex items-center justify-center h-10 w-10 bg-oceanTeal-500 text-white rounded-lg p-2 hover:bg-oceanTeal-600 cursor-pointer">
                            <IconSearch onClick={handleClick} className="h-full w-full" />
                        </div>
                    </div>
                </div>

                {/* Right Section: Image/Placeholder */}
                <div
                    data-aos="zoom-out-left"
                    className="w-full sm-mx:w-full flex items-center justify-center"
                >
                    <div className="w-[30rem] sm-mx:w-full relative">
                        {/* Placeholder for commented-out image and avatar group */}
                        {/*<div className="bg-gray-200 h-64 w-full rounded-lg flex items-center justify-center">*/}
                        {/*    <span className="text-deepSlate-600">Image Placeholder</span>*/}
                        {/*</div>*/}
                        {/* Uncomment and adjust if needed */}
                        {/* <img src="/Boy.png" alt="boy" className="w-full" />
                        <div className="absolute -right-10 bs-mx:right-0 w-fit xs-mx:top-[10%] top-[50%] border-oceanTeal-500 border rounded-lg p-2 xs-mx:-left-5 backdrop-blur-md">
                            <div className="text-center mb-1 text-sm text-deepSlate-900">10K+ got job</div>
                            <Avatar.Group>
                                <Avatar src="/avatar.png" />
                                <Avatar src="/avatar1.png" />
                                <Avatar src="/avatar2.png" />
                                <Avatar>+9K</Avatar>
                            </Avatar.Group>
                        </div>
                        <div className="absolute xs:-left-5 w-fit bs-mx:top-[35%] xs-mx:top-[60%] top-[28%] border-oceanTeal-500 border xs-mx:!right-0 rounded-lg p-2 backdrop-blur-md gap-3 flex flex-col">
                            <div className="flex gap-2 items-center">
                                <div className="w-10 h-10 p-1 bg-deepSlate-100 rounded-lg">
                                    <img src="/Google.png" alt="" />
                                </div>
                                <div className="text-sm text-deepSlate-900">
                                    <div>Software Engineer</div>
                                    <div className="text-deepSlate-600 text-xs">New York</div>
                                </div>
                            </div>
                            <div className="flex gap-2 justify-around text-deepSlate-600 text-xs">
                                <span>1 day ago</span>
                                <span>120 Applicants</span>
                            </div>
                        </div> */}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DreamJob;