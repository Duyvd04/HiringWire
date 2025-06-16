import {
    IconAnchor,
    IconBrandFacebook,
    IconBrandInstagram,
    IconBrandTelegram,
    IconBrandX,
    IconBrandYoutube,
} from "@tabler/icons-react";
import { footerLinks } from "../../Data/Data";
import { useLocation } from "react-router-dom";
import { Divider } from "@mantine/core";
import { Link } from "react-router-dom";


const Footer = () => {
    const location = useLocation();
    return location.pathname !== "/signup" && location.pathname !== "/login" ? (
        <div className="flex flex-col gap-2">
            <div className="pt-20 pb-5 bg-white p-4 flex gap-8 justify-around flex-wrap">
                <div
                    data-aos="fade-up"
                    data-aos-offset="0"
                    className="w-1/4 sm-mx:w-1/3 xs-mx:w-1/2 xsm-mx:w-full flex flex-col gap-4"
                >
                    <div className="flex gap-1 items-center text-oceanTeal-500">
                        {/*<IconAnchor className="h-6 w-6 text-deepSlate-900" stroke={2.5} />*/}
                        <div className="text-xl font-semibold text-deepSlate-900">HiringWire</div>
                    </div>
                    <div className="text-sm text-deepSlate-600">
                        Job portal with user profiles, skill updates, certifications, work experience and admin job postings.
                    </div>
                    <div className="flex gap-3 text-deepSlate-900 [&>a]:bg-deepSlate-100 [&>a]:p-2 [&>a]:rounded-full [&>a]:cursor-pointer hover:[&>a]:bg-deepSlate-200">
                        <a href="https://www.instagram.com">
                            <IconBrandInstagram />
                        </a>
                        <a href="https://t.me/">
                            <IconBrandTelegram />
                        </a>
                        <a href="https://www.youtube.com">
                            <IconBrandYoutube />
                        </a>
                    </div>
                </div>
                {footerLinks.map((item, index) => (
                    <div data-aos-offset="0" data-aos="fade-up" key={index}>
                        <div className="text-lg font-semibold mb-4 text-oceanTeal-500">{item.title}</div>
                        {item.links.map((link, index) => (
                            <Link
                                to={link.path}
                                key={index}
                                className="block text-deepSlate-600 text-sm hover:text-oceanTeal-600 cursor-pointer mb-1 hover:translate-x-2 transition duration-300 ease-in-out"
                            >
                                {link.text}
                            </Link>
                        ))}
                    </div>
                ))}
            </div>
            <Divider color="deepSlate.2" />
            <div
                data-aos="flip-left"
                data-aos-offset="0"
                className="font-medium text-center p-5 bg-white text-deepSlate-900"
            >
                Designed & Developed By{" "}
                <a
                    className="text-oceanTeal-500 hover:text-oceanTeal-600 font-semibold hover:underline"
                    href="https://github.com/Duyvd04"
                >
                    Vu Duc Duy
                </a>
            </div>
        </div>
    ) : (
        <></>
    );
};

export default Footer;