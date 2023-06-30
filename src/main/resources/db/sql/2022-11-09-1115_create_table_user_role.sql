CREATE TABLE public.user_role (
    user_role_ bigserial NOT NULL,
    user_      bigint,
    role_      bigint,
    CONSTRAINT user_role_pkey PRIMARY KEY (user_role_),
    CONSTRAINT user_role_uk UNIQUE (user_, role_),
    CONSTRAINT user_role_user_fkey FOREIGN KEY (user_) REFERENCES public."users" (user_),
    CONSTRAINT user_role_role_fkey FOREIGN KEY (role_) REFERENCES public."roles" (role_)
);